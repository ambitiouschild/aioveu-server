package com.aioveu.oms.service.app.impl;

import com.aioveu.common.result.ResultCode;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.constant.OrderConstants;
import com.aioveu.oms.converter.CartConverter;
import com.aioveu.oms.model.dto.CartItemDto;
import com.aioveu.oms.model.vo.CartItemVo;
import com.aioveu.oms.service.app.CartService;
import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 购物车模块
 * <p>
 * 核心技术：BoundHashOperations
 * 数据格式：
 * -- key <--> 商品列表
 * ---- hKey:value <--> skuId 商品1
 * ---- hKey:value <--> 商品2
 * ---- hKey:value <--> 商品3
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate redisTemplate;
    private final SkuFeignClient skuFeignService;
    private final CartConverter cartConverter;

    /**
     * @Description: 获取会员的购物车信息
     * @Author: 雒世松
     * @Date: 2025/6/20 22:59
     * @param memberId 会员id
     * @return:
     **/
    @Override
    public List<CartItemVo> listCartItemsVo(Long memberId) {
        if (memberId != null) {
            BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
            //把购物车里的购物车所有对象全部取出
            List<CartItemDto> cartItems = cartHashOperations.values();
            if(cartItems == null || cartItems.size() <= 0){
                return Collections.emptyList();
            }
            // 取出购物车里所有的商品id
            List<Long> skuIds = cartItems.stream().map(CartItemDto::getSkuId).distinct().toList();
            //把这些商品信息全部查询出来，为了获取到最新的商品名称也好，商品价格也好 反正我现在是要获取到购物车里的所有商品的最新信息
            List<SkuInfoDTO> skuInfoList = skuFeignService.getSkuInfoList(skuIds);
            //把skuInfoList转为Map。key是id value是SkuInfoDTO
            Map<Long,SkuInfoDTO> temp = new HashMap<Long,SkuInfoDTO>();
            for (SkuInfoDTO skuInfoDTO : skuInfoList) {
                temp.put(skuInfoDTO.getId(),skuInfoDTO);
            }
            //构建改方法需要的返回值，
            List<CartItemVo> cartItemVoList = new ArrayList<>(cartItems.size());
            for (CartItemDto cartItem : cartItems) {
                CartItemVo vo = new CartItemVo();
                vo.setSkuId(cartItem.getSkuId());
//                for (SkuInfoDTO skuInfoDTO : skuInfoList) {
//                    if(Objects.equals(skuInfoDTO.getId(), cartItem.getSkuId())){
//                        //TODO  需要组合一个完整的CartItemVo
//                        vo.setSpuName(skuInfoDTO.getSkuName());
//                        vo.setImageUrl(skuInfoDTO.getPicUrl());
//                        vo.setPrice(skuInfoDTO.getPrice());
//                        break;
//                    }
//                }
                //LeetCode 第一题
                SkuInfoDTO skuInfoDTO = temp.get(cartItem.getSkuId());
                //TODO  skuInfoDTO  NPE 的问题
                vo.setSkuName(skuInfoDTO.getSkuName());
                vo.setSpuName(skuInfoDTO.getSpuName());
                vo.setImageUrl(skuInfoDTO.getPicUrl());
                vo.setPrice(skuInfoDTO.getPrice());
                vo.setCount(cartItem.getCount());
                vo.setChecked(cartItem.getChecked());
                cartItemVoList.add(vo);
            }
            return cartItemVoList;
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    public List<CartItemDto> listCartItems(Long memberId) {
        if (memberId != null) {
            BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
            //把购物车里的购物车所有对象全部取出
            List<CartItemDto> cartItems = cartHashOperations.values();

            return cartItems;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 删除用户购物车(清空购物车)
     */
    @Override
    public boolean deleteCart() {
        String key = OrderConstants.MEMBER_CART_PREFIX + SecurityUtils.getMemberId();
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 添加商品至购物车
     */
    @Override
    public boolean addCartItem(Long skuId) {
        Long memberId = SecurityUtils.getMemberId();
        BoundHashOperations<String, String, CartItemDto> cartHashOperations = getCartHashOperations(memberId);
        String hKey = String.valueOf(skuId);

        CartItemDto cartItem = cartHashOperations.get(hKey);

        if (cartItem != null) {
            // 购物车已存在该商品，更新商品数量
            cartItem.setCount(cartItem.getCount() + 1); // 点击一次“加入购物车”，数量+1
            cartItem.setChecked(true);
        } else {
            // 购物车中不存在该商品，新增商品到购物车
            SkuInfoDTO skuInfo = skuFeignService.getSkuInfo(skuId);
            if (skuInfo != null) {
                cartItem = cartConverter.sku2CartItem(skuInfo);
                cartItem.setCount(1);
                cartItem.setChecked(true);
            }
        }
        cartHashOperations.put(hKey, cartItem);
        return true;
    }

    /**
     * 更新购物车总商品数量、选中状态
     */
    @Override
    public boolean updateCartItem(CartItemDto cartItem) {
        Long memberId;
        try {
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
        String hKey = cartItem.getSkuId() + "";
        if (cartHashOperations.get(hKey) != null) {
            CartItemDto cacheCartItem = (CartItemDto) cartHashOperations.get(hKey);
            if (cartItem.getChecked() != null) {
                cacheCartItem.setChecked(cartItem.getChecked());
            }
            if (cartItem.getCount() != null) {
                cacheCartItem.setCount(cartItem.getCount());
            }
            cartHashOperations.put(hKey, cacheCartItem);
        }
        return true;
    }

    /**
     * 移除购物车的商品
     */
    @Override
    public boolean removeCartItem(Long skuId) {
        Long memberId;
        try {
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
        String hKey = skuId + "";
        cartHashOperations.delete(hKey);
        return true;
    }


    /**
     * 设置商品全选
     */
    @Override
    public boolean checkAll(boolean checked) {
        Long memberId;
        try {
            memberId = SecurityUtils.getMemberId();
        } catch (Exception e) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
        for (Object value : cartHashOperations.values()) {
            CartItemDto cartItem = (CartItemDto) value;
            cartItem.setChecked(checked);
            String hKey = cartItem.getSkuId() + "";
            cartHashOperations.put(hKey, cartItem);
        }
        return true;
    }


    /**
     * 移除购物车选中的商品，作用场景：
     * <p>
     * 1.支付后删除购物车的商品
     */
    @Override
    public boolean removeCheckedItem() {
        Long memberId = SecurityUtils.getMemberId();
        if (memberId == null) {
            throw new BizException(ResultCode.TOKEN_INVALID);
        }
        BoundHashOperations cartHashOperations = getCartHashOperations(memberId);
        for (Object value : cartHashOperations.values()) {
            CartItemDto cartItem = (CartItemDto) value;
            if (cartItem.getChecked()) {
                cartHashOperations.delete(cartItem.getSkuId() + "");
            }
        }
        return true;
    }

    /**
     * 获取第一层，即某个用户的购物车
     */
    private BoundHashOperations getCartHashOperations(Long memberId) {
        String cartKey = OrderConstants.MEMBER_CART_PREFIX + memberId;
        BoundHashOperations operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }
}
