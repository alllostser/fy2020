package com.neuedu.utils;

import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.OrderItemVo;
import com.neuedu.pojo.vo.OrderVo;
import com.neuedu.pojo.vo.ProductVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoToVoUtil {
    /**
     * 商品转商品vo
     * @param product
     * @return
     * @throws IOException
     */
    public static ProductVo productToProductVo(Product product) throws IOException {
        ProductVo vo = new ProductVo();
        vo.setImageHost(PropertiesUtil.getProperty("imageHost"));
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setSubtitle(product.getSubtitle());
        vo.setMainImage(product.getMainImage());
        vo.setSubImages(product.getSubImages());
        vo.setDetail(product.getDetail());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
//        商品状态，1-在售 2-下架 3-删除
        String status = "";
        switch (product.getStatus()) {
            case 1:
                status = "在售";
                break;
            case 2:
                status = "已下架";
                break;
            case 3:
                status = "已删除";
                break;
        }
        vo.setStatus(status);
        vo.setCreateTime(TimeUtils.dateToStr(product.getCreateTime()));
        vo.setUpdateTime(TimeUtils.dateToStr(product.getUpdateTime()));
        return vo;
    }

    /**
     * order-->OrderVo
     * @param order
     * @param orderItemList
     * @return
     */
    public static OrderVo orderToOrderVo(Order order, List<OrderItem> orderItemList,Integer shippingId){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTime(TimeUtils.dateToStr(order.getPaymentTime()));
        orderVo.setPostage(order.getPostage());
        orderVo.setSendTime(TimeUtils.dateToStr(order.getSendTime()));
        orderVo.setShippingId(shippingId);
//        订单状态：0-已取消 10-未付款 20-已付款 40-已发货 50-交易成功 60-交易关闭
        String status = "";
        switch (order.getStatus()){
            case 0:
                status="已取消";
                break;
            case 10:
                status="未付款";
                break;
            case 20:
                status="已付款";
                break;
            case 40:
                status="已发货";
                break;
            case 50:
                status="交易成功";
                break;
            case 60:
                status="交易关闭";
                break;
        }
        orderVo.setStatus(status);
        orderVo.setCloseTime(TimeUtils.dateToStr(order.getCloseTime()));
        orderVo.setCreateTime(TimeUtils.dateToStr(order.getCreateTime()));

        List<OrderItemVo> orderItemVos = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = PoToVoUtil.orderItemToOrderItemVo(orderItem);
            orderItemVos.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVos);
        return orderVo;
    }

    /**
     * order-->OrderVo
     * @param order
     * @param orderItemList
     * @return
     */
    public static OrderVo orderToOrderVo(Order order, List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTime(TimeUtils.dateToStr(order.getPaymentTime()));
        orderVo.setPostage(order.getPostage());
        orderVo.setSendTime(TimeUtils.dateToStr(order.getSendTime()));
        orderVo.setShippingId(order.getShippingId());
        String status = "";
        switch (order.getStatus()){
            case 0:
                status="已取消";
                break;
            case 10:
                status="未付款";
                break;
            case 20:
                status="已付款";
                break;
            case 40:
                status="已发货";
                break;
            case 50:
                status="交易成功";
                break;
            case 60:
                status="交易关闭";
                break;
        }

        orderVo.setStatus(status);
        orderVo.setCloseTime(TimeUtils.dateToStr(order.getCloseTime()));
        orderVo.setCreateTime(TimeUtils.dateToStr(order.getCreateTime()));

        List<OrderItemVo> orderItemVos = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = PoToVoUtil.orderItemToOrderItemVo(orderItem);
            orderItemVos.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVos);
        return orderVo;
    }

    /**
     * orderItem-->OrderItemVo
     * @param orderItem
     * @return
     */
    public static OrderItemVo orderItemToOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setCreateTime(TimeUtils.dateToStr(orderItem.getCreateTime()));

        return orderItemVo;
    }

}
