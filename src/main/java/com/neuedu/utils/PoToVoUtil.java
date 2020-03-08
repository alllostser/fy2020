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
        vo.setStatus(product.getStatus());
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
        orderVo.setStatus(order.getStatus());
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
