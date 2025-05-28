package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>微信小程序审核状态</h1>
 * https://developers.weixin.qq.com/doc/oplatform/openApi/OpenApiDoc/miniprogram-management/code-management/getAuditStatus.html
 * @author: luyao
 */
@Getter
@AllArgsConstructor
public enum WxOpenSubmitAuditStatus {

    weapp_audit_success("审核成功", 0),
    weapp_audit_fail("审核被拒绝", 1),
    weapp_audit("审核中", 2),
    weapp_audit_revoke("已撤回", 3),
    weapp_audit_delay("审核延后", 4);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static WxOpenSubmitAuditStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElse(null);
    }

    @Override
    public String toString() {
        return this.getCode().toString();
    }
}
