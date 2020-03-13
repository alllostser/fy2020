package com.neuedu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.OrderDao;
import com.neuedu.dao.OrderItemDao;
import com.neuedu.dao.PayInfoDao;
import com.neuedu.pojo.*;
import com.neuedu.pojo.vo.OrderItemVo;
import com.neuedu.pojo.vo.OrderVo;
import com.neuedu.pojo.vo.ProductVo;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private ICartService cartService;
    @Resource
    private IProductService productService;
    /*  前台模块  */


    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    @Transactional
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //step1：参数非空校验
        if (shippingId == null || shippingId<0){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ADDRESS_ISEMPTY.getStatus(),Consts.OrderStatusEnum.ADDRESS_ISEMPTY.getDesc());
        }
        //step2:根据userId查询购物车中已经选中的商品
        ServerResponse<List<Cart>> response = cartService.findCartByUserIdAndChecked(userId);
        if (!response.isSucess()){
            return response;
        }
        List<Cart> cartList = response.getData();
        if (cartList == null || cartList.size()<=0){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.USER_CART_ISEMPTY.getStatus(),Consts.OrderStatusEnum.USER_CART_ISEMPTY.getDesc());
        }
        //step3:将List<Cart>转化为List<OrderItem>
        ServerResponse serverResponse = assembleOrderItemList(cartList, userId);
        if (!serverResponse.isSucess()){
            return serverResponse;
        }
        //step4:生成订单，并插入订单库
        List<OrderItem> itemList = (List<OrderItem>) serverResponse.getData();
        ServerResponse generateOrder = generateOrder(userId, itemList, shippingId);
        if (!generateOrder.isSucess()){
            return generateOrder;
        }
        //step5:将订单明细批量插入订单明细库
        Order order = (Order) generateOrder.getData();
        for (OrderItem orderItem : itemList) {
            orderItem.setOrderNo(order.getOrderNo());

        }
        int insertCount = orderItemDao.insertBatch(itemList);
        if (insertCount<=0){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.CREATE_ORDERITEM_FAILED.getStatus(),Consts.OrderStatusEnum.CREATE_ORDERITEM_FAILED.getDesc());
        }
        //step6:商品减库存
        ServerResponse sr = updateSotck(itemList,0);//0为减库存
        if (!sr.isSucess()){
            return sr;
        }
        //step7:清除购物车中已经下单的商品
        ServerResponse msg = cleanCart(cartList);
        if (!msg.isSucess()){
            return msg;
        }
        //step8:前端返回orderVo
        OrderVo orderVo = PoToVoUtil.orderToOrderVo(order, itemList, shippingId);
        return ServerResponse.serverResponseBySucess(orderVo);
    }
    /**
     * 将List<Cart>转化为List<OrderItem>
     * @param cartList
     * @return
     */
    private  ServerResponse assembleOrderItemList(List<Cart> cartList,Integer userId){
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUserId(userId);
            //根据商品id查询商品信息
            ServerResponse<ProductVo> ProductDetail = productService.detail(cart.getProductId(), 0, 0, 0);
            if (!ProductDetail.isSucess()){
                return ProductDetail;
            }
            //查询商品是否处于在售状态
            ProductVo productVo = ProductDetail.getData();
            Integer status = null;
            switch (productVo.getStatus()) {
                case "在售":
                    status = 1;
                    break;
                case "已下架":
                    status = 2;
                    break;
                case "已删除":
                    status = 3;
                    break;
            }
            if (status!= Consts.ProductStatusEnum.ON_SALE.getStatus()){
                return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getStatus(),Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getDesc());
            }
            //判断商品库存是否充足
            if (productVo.getStock()<cart.getQuantity()){
                return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_UNDERSTOCK.getStatus(),Consts.ProductStatusEnum.PRODUCT_UNDERSTOCK.getDesc());
            }
            orderItem.setCurrentUnitPrice(productVo.getPrice());
            orderItem.setProductImage(productVo.getMainImage());
            orderItem.setProductName(productVo.getName());
            orderItem.setTotalPrice(BigDecimalUtil.multi(String.valueOf(productVo.getPrice()),String.valueOf(cart.getQuantity())));
            orderItems.add(orderItem);
        }
        return ServerResponse.serverResponseBySucess(orderItems);
    }
    /**
     * 生成订单
     * */
    private ServerResponse generateOrder(Integer userId,List<OrderItem> orderItems,Integer shippingId){
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //订单总金额
        order.setPayment(BigDecimalUtil.getOrderTotalPrice(orderItems));
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(Consts.OrderStatusEnum.UNPAY.getStatus());
        order.setOrderNo(OrderNoUtil.generateOrderNo());
        //将订单落库
        int insertCount = orderDao.insert(order);
        if (insertCount<=0){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.CREATE_ORDER_FAILED.getStatus(),Consts.OrderStatusEnum.CREATE_ORDER_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(order);
    }

    /**
     * 商品更新库存
     * @param orderItems
     * @param type 1为加库存 0为减库存
     * @return
     */
    private ServerResponse updateSotck(List<OrderItem> orderItems, Integer type){
        ServerResponse serverResponse = null;
        for (OrderItem orderItem : orderItems) {
            Integer productId = orderItem.getProductId();
            Integer quantity = orderItem.getQuantity();
            //根据商品id更新库存
             serverResponse = productService.updateStock(productId, quantity,type);
            if (!serverResponse.isSucess()){
                return serverResponse;
            }
        }
        return serverResponse;
    }

    /**
     * 清除购物车中已下单的商品
     * @param cartList
     * @return
     */
    private ServerResponse cleanCart(List<Cart> cartList){
       if (cartList == null || cartList.size()<=0){
           return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
       }
       ServerResponse serverResponse =  cartService.deleteBatchByIds(cartList);
       if (!serverResponse.isSucess()){
           return serverResponse;
       }
       return serverResponse;
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse cancelOrder(Long orderNo) {
        //step1：非空判断
        if (orderNo == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //step2：根据订单号查询订单是否存在
        Order order = orderDao.findOrderByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ORDER_NOT_EXISTS.getStatus(),Consts.OrderStatusEnum.ORDER_NOT_EXISTS.getDesc());
        }
            //只有未付款的订单才能取消
        if (order.getStatus() == Consts.OrderStatusEnum.PAYED.getStatus()
            || order.getStatus() == Consts.OrderStatusEnum.SEND.getStatus()){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ORDER_IS_INDEFEASIBLE.getStatus(),Consts.OrderStatusEnum.ORDER_IS_INDEFEASIBLE.getDesc());
        }else if (order.getStatus() == Consts.OrderStatusEnum.CANCELED.getStatus()){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ORDER_ALREADY_CANCELLED.getStatus(),Consts.OrderStatusEnum.ORDER_ALREADY_CANCELLED.getDesc());
        }
        order.setStatus(Consts.OrderStatusEnum.CANCELED.getStatus());
        int updateCount = orderDao.updateByPrimaryKeySelective(order);
        if (updateCount<=0){
            //订单取消失败
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ORDER_CANCEL_FAILED.getStatus(),Consts.OrderStatusEnum.ORDER_CANCEL_FAILED.getDesc());
        }
        //step3:更新库存
            //根据订单号查询订单详情
        List<OrderItem> orderItemList = orderItemDao.findOrderItemByOrderNo(orderNo);
        ServerResponse serverResponse = updateSotck(orderItemList, 1);
        if (!serverResponse.isSucess()){
            return serverResponse;
        }
        return ServerResponse.serverResponseBySucess("取消成功",updateCount);
    }

    /**
     *获取订单列表（用户）
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse orderList(Integer userId, Integer pageNum, Integer pageSize) {
        //step1：非空判断
        if (userId == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderDao.orderListByUserId(userId);
        List<OrderVo> orderVos = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemDao.findOrderItemByOrderNo(order.getOrderNo());
            OrderVo orderVo = PoToVoUtil.orderToOrderVo(order, orderItemList);
            orderVos.add(orderVo);
        }
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orderVos);
        return ServerResponse.serverResponseBySucess(pageInfo);
    }

    /**
     * 用户支付
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse orderPay(Integer userId, Long orderNo) {
        //step:1参数非空校验
        if (orderNo == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Order order= orderDao.findOrderByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.ORDER_NOT_EXISTS.getStatus(),Consts.OrderStatusEnum.ORDER_NOT_EXISTS.getDesc());
        }
        List<OrderItem> orderItemList = orderItemDao.findOrderItemByOrderNo(order.getOrderNo());
        OrderVo orderVo = PoToVoUtil.orderToOrderVo(order, orderItemList);
        ServerResponse serverResponse = AlipayUtil.trade_precreate(orderVo);
        return serverResponse;
    }

    @Resource
    private PayInfoDao payInfoDao;
    /**
     * 支付宝回调
     * @param signMap
     * @return
     */
    @Override
    public String alipayCallback(Map<String, String> signMap) {
        //step1:获取商家订单号
        long orderNo = Long.parseLong(signMap.get("out_trade_no"));

        //step2:根据订单号查询订单
        Order order = orderDao.findOrderByOrderNo(orderNo);
        if (order == null){
            return "fail";
        }
        List<OrderItem> orderItemList = orderItemDao.findOrderItemByOrderNo(order.getOrderNo());
        OrderVo orderVo = PoToVoUtil.orderToOrderVo(order, orderItemList);
        String status = orderVo.getStatus();
        if(!status.equals("已取消") && !status.equals("未付款")){
            //支付宝回调接口已经修改过订单状态了
            return "success";
        }
        //step3:修改订单状态
        String tradeStatus = signMap.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus)){//订单支付成功
            //修改订单状态
             String gmt_payment = signMap.get("gmt_payment");
            Date payTime = TimeUtils.strToDate(gmt_payment);
            int updateCount = orderDao.updateOrderStatus(orderNo,payTime,Consts.OrderStatusEnum.PAYED.getStatus());
            if (updateCount<=0){
                return "fail";
            }
        }
        if ("WAIT_BUYER_PAY".equals(tradeStatus)){//如果是创建订单成功的回调
            return "success";
        }
        //step4:支付信息插入支付宝
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setPlatformNumber(signMap.get("trade_no"));
        payInfo.setPayPlatform(Consts.PayInfoEnum.ZHIFUBAO.getStatus());
        payInfo.setOrderNo(orderNo);
        payInfo.setPlatformStatus(signMap.get("trade_status"));
        int insertCount = payInfoDao.insert(payInfo);
        if (insertCount <= 0){
            return "fail";
        }
        return "success";
    }
}
