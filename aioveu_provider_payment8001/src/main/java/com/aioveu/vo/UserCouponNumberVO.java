package com.aioveu.vo;

import com.aioveu.dto.UserCouponDTO;
import lombok.Data;

import java.util.List;

/**
 * @author xlfan10
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserCouponNumberVO {

    private Long couponId;

    private String couponName;

    private Long used;

    private Long unUsed;

    private Long expired;

    private Long freeze;

    private List<UserCouponDTO> userCouponList;
}
