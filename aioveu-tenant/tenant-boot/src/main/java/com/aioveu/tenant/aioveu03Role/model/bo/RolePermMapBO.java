package com.aioveu.tenant.aioveu03Role.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: RolePermMapBO
 * @Description TODO 角色权限集合
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/31 18:55
 * @Version 1.0
 **/

/*
* ❌ 没有 tenantId
✅ 语义只服务于认证
✅ 和 PremRolesMap职责分离
👉 这是“架构级正确”
*
*
✅ 三表全隔离
✅ 不依赖 MP
✅ Feign 调用 100% 安全
*
*
✅ 4️认证接口参数驱动 ✅
❌ 不依赖 TenantContextHolder
❌ 不依赖 SecurityContext
✅ tenantId 来自方法参数
👉 这是多租户认证 SQL 的“标准答案”
*
* */
@Schema(description = "角色权限集合")
@Data
public class RolePermMapBO {

    private String roleCode;
    private Set<String> perms = new HashSet<>();
}
