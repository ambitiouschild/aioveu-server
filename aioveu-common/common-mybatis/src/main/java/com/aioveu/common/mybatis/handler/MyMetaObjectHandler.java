package com.aioveu.common.mybatis.handler;


import com.aioveu.common.core.tenant.TenantContextHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description: TODO mybatis-plus 字段自动填充
 *                      MyMetaObjectHandler= MyBatis‑Plus 的 ORM 基础设施
 * https://mp.baomidou.com/guide/auto-fill-metainfo.html
 * @Author: 雒世松
 * @Date: 2025/6/5 15:52
 * @param
 * @return:
 **/
/**
 * MyBatis-Plus 自动填充处理器（基础设施层）
 * <p>
 * 原则：
 * 1. 只消费 TenantContextHolder，不解析 JWT
 * 2. 不依赖 security-resource
 * 3. tenantId 允许为 null（平台级 / auth 场景）
 * 4. 不强制填充，由 TenantLineHandler 控制 SQL
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyMetaObjectHandler implements MetaObjectHandler {


    /**
     * 新增填充创建时间、更新时间和租户ID
     * <p>
     * 多租户模式下，tenant_id 字段的 exist 属性会被 TenantDynamicFieldConfig 动态设置为 true，
     * 因此这里的 strictInsertFill 可以正常工作
     * </p>
     *
     * @param metaObject 元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        log.debug("【MyMetaObjectHandler】insertFill: {}",
                metaObject.getOriginalObject().getClass().getSimpleName());

        // 填充创建时间
        this.strictInsertFill(metaObject,
                "createTime",
                () -> LocalDateTime.now(),
                LocalDateTime.class);
        // 填充更新时间
        this.strictUpdateFill(metaObject,
                "updateTime",
                () -> LocalDateTime.now(),
                LocalDateTime.class);


        // 租户ID（非强制）
        // 强制多租户：仅当上下文存在 tenantId 时自动填充
        //✅ 不要为了“看起来完整”强行填 tenantId
        Long tenantId = TenantContextHolder.getTenantId();

        if (tenantId != null) {
            // 有租户ID，填充
            Long finalTenantId = tenantId;
            this.strictInsertFill(
                    metaObject,
                    "tenantId",
                    () -> finalTenantId,
                    Long.class);
        }

    }

    /**
     * 更新填充更新时间
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("【MyMetaObjectHandler】updateFill: {}",
                metaObject.getOriginalObject().getClass().getSimpleName());

        this.strictUpdateFill(metaObject,
                "updateTime",
                () -> LocalDateTime.now(),
                LocalDateTime.class);
    }

}
