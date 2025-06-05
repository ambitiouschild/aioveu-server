package com.aioveu.oms.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.model.dto.CartItemDto;
import com.aioveu.oms.service.app.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO App-购物车接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:05
 * @param
 * @return:
 **/


@Tag(name  = "App-购物车接口")
@RestController
@RequestMapping("/app-api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "查询购物车")
    @GetMapping
    public <T> Result<T> getCart() {
        List<CartItemDto> result = cartService.listCartItems(SecurityUtils.getMemberId());
        return Result.success((T) result);
    }

    @Operation(summary = "删除购物车")
    @DeleteMapping
    public <T> Result<T> deleteCart() {
        boolean result = cartService.deleteCart();
        return Result.judge(result);
    }

    @Operation(summary = "添加购物车商品")
    @PostMapping
    public <T> Result<T> addCartItem(@RequestParam Long skuId) {
        cartService.addCartItem(skuId);
        return Result.success();
    }

    @Operation(summary = "更新购物车商品")
    @PutMapping("/skuId/{skuId}")
    public <T> Result<T> updateCartItem(
            @PathVariable Long skuId,
            @RequestBody CartItemDto cartItem
    ) {
        cartItem.setSkuId(skuId);
        boolean result = cartService.updateCartItem(cartItem);
        return Result.judge(result);
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/skuId/{skuId}")
    public <T> Result<T> removeCartItem(@PathVariable Long skuId) {
        boolean result = cartService.removeCartItem(skuId);
        return Result.judge(result);
    }

    @Operation(summary = "全选/全不选购物车商品")
    @PatchMapping("/_check")
    public <T> Result<T> check(
         @Parameter(name ="全选/全不选") boolean checked
    ) {
        boolean result = cartService.checkAll(checked);
        return Result.judge(result);
    }
}
