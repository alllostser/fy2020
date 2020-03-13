package com.neuedu.service.Impl;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingDao;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingSerivce;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShippingServiecImpl implements IShippingSerivce {
    @Resource
    private ShippingDao dao;

    /**
     * 添加用户收货地址
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse add(Shipping shipping) {
        if (shipping.getReceiverName() == null || "".equals(shipping.getReceiverName())){
            return ServerResponse.serverResponseByFail(Consts.shippingStatusEnum.RECEIVERNAME_NOT_BE_EMPTY.getStatus(),Consts.shippingStatusEnum.RECEIVERNAME_NOT_BE_EMPTY.getDesc());
        }
        if ((shipping.getReceiverMobile() == null || "".equals(shipping.getReceiverMobile())) && (shipping.getReceiverPhone() ==null || "".equals(shipping.getReceiverPhone()))){
            return ServerResponse.serverResponseByFail(Consts.shippingStatusEnum.RECEIVERPHONE_NOT_BE_EMPTY.getStatus(),Consts.shippingStatusEnum.RECEIVERPHONE_NOT_BE_EMPTY.getDesc());
        }
        if (shipping.getReceiverAddress() == null || "".equals(shipping.getReceiverAddress())){
            return ServerResponse.serverResponseByFail(Consts.shippingStatusEnum.RECEIVERADDRESS_NOT_BE_EMPTY.getStatus(),Consts.shippingStatusEnum.RECEIVERADDRESS_NOT_BE_EMPTY.getDesc());
        }
        int insertCount = dao.insertSelective(shipping);
        if (insertCount <= 0){
            return ServerResponse.serverResponseByFail(Consts.shippingStatusEnum.ADDRESS_ADD_FAIL.getStatus(),Consts.shippingStatusEnum.ADDRESS_ADD_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySucess(shipping.getId());
    }
}
