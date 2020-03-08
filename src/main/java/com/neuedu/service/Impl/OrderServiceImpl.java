package com.neuedu.service.Impl;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.OrderDao;
import com.neuedu.dao.OrderItemDao;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.vo.OrderVo;
import com.neuedu.pojo.vo.ProductVo;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import com.neuedu.utils.OrderNoUtil;
import com.neuedu.utils.PoToVoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        ServerResponse sr = reduceSotck(itemList);
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
            if (productVo.getStatus()!= Consts.ProductStatusEnum.ON_SALE.getStatus()){
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
     * 商品减库存
     * @param orderItems
     * @return
     */
    private ServerResponse reduceSotck(List<OrderItem> orderItems){
        ServerResponse serverResponse = null;
        for (OrderItem orderItem : orderItems) {
            Integer productId = orderItem.getProductId();
            Integer quantity = orderItem.getQuantity();
            //根据商品id扣库存
             serverResponse = productService.reduceStock(productId, quantity);
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


}
