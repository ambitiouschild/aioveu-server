package com.aioveu.oms.service.app;

import com.aioveu.oms.model.dto.CartItemDto;

import java.util.List;

/**
 * @Description: TODO 购物车业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:14
 * @param
 * @return:
 **/

public interface CartService {

    List<CartItemDto> listCartItems(Long memberId);

    boolean deleteCart();

    boolean addCartItem(Long skuId);

    boolean updateCartItem(CartItemDto cartItem);

    boolean removeCartItem(Long skuId);

    boolean removeCheckedItem();

    boolean checkAll(boolean checked);

}
