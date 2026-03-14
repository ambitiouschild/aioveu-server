package com.aioveu.common.constant;

public interface JwtClaimConstants {


    /**
     * 令牌类型
     */
    String TOKEN_TYPE = "tokenType";

    /**
     * 用户ID
     */
    String USER_ID = "userId";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 部门ID
     */
    String DEPT_ID = "deptId";

    /**
     * 数据权限
     */
    String DATA_SCOPE = "dataScope";


    /**
     * 数据权限列表（多角色）
     */
    String DATA_SCOPES = "dataScopes";

    /**
     * 权限(角色Code)集合
     */
    String AUTHORITIES = "authorities";

    /**
     * 租户ID
     */
    String TENANT_ID= "tenant_id";

    /**
     * 是否可以切换租户
     */
    String CAN_SWITCH_TENANT= "can_switch_tenant";



    /**
     * 会员ID
     */
    String MEMBER_ID = "memberId";

    /**
     * Token 版本号
     * <p>
     * 用于用户级会话失效，当用户修改密码、被禁用、强制下线时递增版本号，
     * 使该用户之前签发的所有 Token 失效。
     */
    String TOKEN_VERSION = "tokenVersion";
}
