package com.aioveu.tenant.aioveu01Tenant.service;


/**
 * @ClassName: TenantManagerMenuInitService
 * @Description TODO 初始化租户服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/1 22:22
 * @Version 1.0
 **/

public interface TenantManagerMenuInitService {


    //租户第一次创建时，把平台模板“复制一份”到自己名下

    /*
    *   是的，如果你在「租户第一次创建时」就把平台模板完整复制一份到自己名下：
        ✅ 你现在这套
        租户优先 → 平台兜底
        就完全没有存在价值了
        ❌ 甚至可以说：留着是多余的，还可能制造混乱
    *
    * */

    /*
    *       可以，而且这是最推荐的过渡方案：
            ✅ 老租户 → 用「平台兜底查询」
            ✅ 新租户 → 初始化时 copy 平台模板
            ✅ 某一天 → 全量刷一次老数据
            ✅ 最后 → 删除平台兜底逻辑
👉 这不是妥协，这是“平滑迁移”的标准做法。
    *
    *
    * */

    /**
     * 初始化租户管理端菜单（从平台模板复制）
     */
    void initTenantManagerMenu(Long tenantId);
}
