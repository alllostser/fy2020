package com.neuedu.service.Impl;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.CartDao;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.vo.CartProductVo;
import com.neuedu.pojo.vo.CartVo;
import com.neuedu.pojo.vo.ProductVo;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Resource
    private CartDao cartDao;
    @Resource
    private IProductService productService;


    /**
     * 购物车添加商品
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        //step1:参数非空判断
        if (productId == null || count == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(), StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }

        //step2:根据商品id查询商品是否存在
        ServerResponse<ProductVo> serverResponse = productService.detail(productId, 0, 0, 0);
        if (!serverResponse.isSucess()) {
            return serverResponse;
        }
        //step3:根据用户id和商品id查询购物车中是否包含此商品
        Cart cart = cartDao.findCartByUserIdAndProductId(userId, productId);
        if (cart == null) {//该商品第一次加入购物车
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setChecked(Consts.CartProductEnum.CHECKED.getStatus());
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            //添加购物车
            int result = cartDao.insert(newCart);
            if (result>0){ //添加成功
                return ServerResponse.serverResponseBySucess(getCartVo(userId));
            }else {//添加失败
                return ServerResponse.serverResponseByFail(Consts.CartProductEnum.ADD_CART_FAILED.getStatus(),Consts.CartProductEnum.ADD_CART_FAILED.getDesc());
            }
        } else {//该商品已经在购物车中
            cart.setQuantity(cart.getQuantity()+count);
            int result = cartDao.updateByPrimaryKey(cart);
            if (result>0){
                return ServerResponse.serverResponseBySucess(getCartVo(userId));
            }else{
                return ServerResponse.serverResponseByFail(Consts.CartProductEnum.UPDATE_CART_FAILED.getStatus(),Consts.CartProductEnum.UPDATE_CART_FAILED.getDesc());
            }
        }
    }


    /**
     * 购物车列表
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse list(Integer userId) {
        CartVo cartVo = getCartVo(userId);

        return ServerResponse.serverResponseBySucess(cartVo);
    }

    private CartVo getCartVo(Integer userId) {
        CartVo cartVo = new CartVo();
        //step1:根据用户id获取购物车信息-->List<Cart>
        List<Cart> cartList = cartDao.findCartByUserId(userId);
        if (cartList == null || cartList.size() <= 0) {
            return cartVo;
        }
        //step2:将List<Cart>转换为List<CartProductVo>
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        //定义购物车总价格变量
        BigDecimal cartTotaPrice = new BigDecimal("0");
        for (Cart cart : cartList) {
            CartProductVo cartProductVo = new CartProductVo();
            cartProductVo.setId(cart.getId());
            cartProductVo.setProductId(cart.getProductId());
            cartProductVo.setProductChecked(cart.getChecked());
            cartProductVo.setUserId(cart.getUserId());
            //根据商品id查询商品信息
            ServerResponse<ProductVo> detail = productService.detail(cart.getProductId(), 0, 0, 0);
            if (!detail.isSucess()) {
                continue;
            }
            ProductVo data = detail.getData();
            cartProductVo.setProductMainImage(data.getMainImage());
            cartProductVo.setProductName(data.getName());
            cartProductVo.setProductPrice(data.getPrice());
            cartProductVo.setProductStock(data.getStock());
            cartProductVo.setProductSubtitle(data.getSubtitle());

            Integer quantity = cart.getQuantity();
            Integer realStock = data.getStock();

            if (realStock >= quantity) {
                //库存充足
                cartProductVo.setLimitQuantity("LIMIT_NUM_SUCCESS");
                cartProductVo.setQuantity(cart.getQuantity());
            } else {
                //库存不足
                //修改该商品在购物车中数量，根据商品id
                int updateCount = cartDao.updateByProductId(userId,cartProductVo.getProductId(), realStock);
                if (updateCount <= 0) {
                    //修改失败
                    continue;
                }
                cartProductVo.setLimitQuantity("LIMIT_NUM_FAIL");
                cartProductVo.setQuantity(realStock);
            }
            //计算已选商品总价格
            cartProductVo.setProductTotalPrice(BigDecimalUtil.multi(String.valueOf(data.getPrice().doubleValue()), String.valueOf(cartProductVo.getQuantity())));
            if (cartProductVo.getProductChecked() == Consts.CartProductEnum.CHECKED.getStatus()) {
                //已选中
                cartTotaPrice = BigDecimalUtil.add(String.valueOf(cartTotaPrice.doubleValue()), String.valueOf(cartProductVo.getProductTotalPrice().doubleValue()));
            }
            cartProductVoList.add(cartProductVo);
        }
        cartVo.setProductVoList(cartProductVoList);
        //step3:计算购物车总价格
        cartVo.setCartTotalPrice(cartTotaPrice);
        //step4:验证是否全选
        int result = cartDao.totalCountByUnchecked(userId);
        if (result > 0) {//购物车中有未选中的商品
            cartVo.setAllChecked(false);
        } else {
            cartVo.setAllChecked(true);
        }
        //step5：返回CartVo
        return cartVo;
    }

    /**
     * 查询购物车中用户已经选中的商品
     * @param userId
     * @return
     */
    @Override
    public ServerResponse findCartByUserIdAndChecked(Integer userId) {
        //参数非空判断
        if (userId == null){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        List<Cart> cartList = cartDao.findCartByUserIdAndChecked(userId);

        return ServerResponse.serverResponseBySucess(cartList);
    }

    /**
     * 清除购物车中已下单的商品
     * @param cartList
     * @return
     */
    @Override
    public ServerResponse deleteBatchByIds(List<Cart> cartList) {
        if (cartList == null || cartList.size()<=0){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        int count = cartDao.deleteBach(cartList);
        if (count<=0){
            return ServerResponse.serverResponseByFail(Consts.CartProductEnum.CLEAN_CART_FAILED.getStatus(),Consts.CartProductEnum.CLEAN_CART_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess("清除成功");
    }
}
