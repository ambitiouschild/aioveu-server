package com.aioveu.oms.aioveu01Order.service;

import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.aioveu01Order.model.vo.CartItemDto;
import com.aioveu.oms.aioveu01Order.model.vo.CartItemVo;

import java.util.List;

/**
 * @Description: TODO 购物车业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:14
 * @param
 * @return:
 **/

public interface CartService {


    /**
     *   TODO       获取用户购物车中的所有商品列表
     *
     * @param memberId 用户ID
     * @return 购物车商品列表，如果用户ID为空返回空列表
     */
    List<CartItemVo> listCartItemsVo(Long memberId);


    /**
     *   TODO       获取用户购物车中的所有商品列表
     *
     * @param memberId 用户ID
     * @return 购物车商品列表，如果用户ID为空返回空列表
     */
    List<CartItemDto> listCartItems(Long memberId);



    boolean deleteCart();

    /**
     * TODO   添加商品到购物车
     *
     * @param skuId 商品SKU ID
     * @param count 添加商品到购物车商品数量
     * @return 添加成功返回true
     * @throws BizException 如果商品信息获取失败会抛出异常
     */
    boolean addCartItem(Long skuId , Integer count);

    boolean updateCartItem(CartItemDto cartItem);

    boolean removeCartItem(Long skuId);

    boolean removeCheckedItem();

    boolean checkAll(boolean checked);

}
