package com.aioveu.oms.aioveu01Order.service;

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

    List<CartItemVo> listCartItemsVo(Long memberId);

    List<CartItemDto> listCartItems(Long memberId);

    boolean deleteCart();

    boolean addCartItem(Long skuId);

    boolean updateCartItem(CartItemDto cartItem);

    boolean removeCartItem(Long skuId);

    boolean removeCheckedItem();

    boolean checkAll(boolean checked);

}
