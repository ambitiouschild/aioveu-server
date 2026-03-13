package com.aioveu.common.mybatis.handler;

import com.aioveu.common.mybatis.config.property.TenantProperties;
import com.aioveu.common.tenant.TenantContextHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description: TODO mybatis-plus 字段自动填充
 * https://mp.baomidou.com/guide/auto-fill-metainfo.html
 * @Author: 雒世松
 * @Date: 2025/6/5 15:52
 * @param
 * @return:
 **/

@Component
@RequiredArgsConstructor
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired(required = false)
    private TenantProperties tenantProperties;


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

        System.out.println("=== MyMetaObjectHandler.insertFill ===");
        System.out.println("对象: " + metaObject.getOriginalObject().getClass().getName());

        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);



        // 强制多租户：仅当上下文存在 tenantId 时自动填充
        if (tenantProperties != null) {
            Long tenantId = TenantContextHolder.getTenantId();
            System.out.println("租户ID: " + tenantId);
            if (tenantId != null) {
                // 有租户ID，填充
                Long finalTenantId = tenantId;
                this.strictInsertFill(metaObject, "tenantId", () -> finalTenantId, Long.class);
                System.out.println("【MyMetaObjectHandler】填充租户ID: " + tenantId);
            }else {
                // 没有租户ID，不填充
                System.out.println("【MyMetaObjectHandler】不填充租户ID");
            }
        }
    }

    /**
     * 更新填充更新时间
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }

}
