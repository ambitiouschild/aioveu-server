package com.aioveu.common.constant;



/*

JwtClaimConstants里定义的，不是“值”，而是“JWT 里的字段名”
所以它必须全是 String

真正的类型是在“使用的地方”决定的
JWT 里写的时候（Provider / Customizer）

claims.claim(JwtClaimConstants.USER_ID, 10001L);      // value = Long
claims.claim(JwtClaimConstants.CLIENT_ID, "web");    // value = String
claims.claim(JwtClaimConstants.TENANT_ID, 888L);     // value = Long
claims.claim(JwtClaimConstants.TOKEN_VERSION, 3L);    // value = Long

JWT 里读的时候（Gateway / 资源服务器）

String clientId = jwt.getClaimAsString(JwtClaimConstants.CLIENT_ID); // ✅ String
Long userId = jwt.getClaim(JwtClaimConstants.USER_ID, Long.class);    // ✅ Long
Long tenantId = jwt.getClaim(JwtClaimConstants.TENANT_ID, Long.class); // ✅ Long
Long tokenVersion = jwt.getClaim(JwtClaimConstants.TOKEN_VERSION, Long.class); // ✅ Long

把“业务域”分得更清楚（非必须）
✅ 可读性更强
✅ 不容易写错 key
✅ 非常适合平台级系统

 */
public interface JwtClaimConstants {



    /** 用户域 */
    interface User {
        /**用户ID*/
        String ID = "userId";
        /* 用户名 */
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
    }

    /** 租户域 */
    interface Tenant {
        /**
         * 租户ID
         */
        String ID = "tenant_id";
        /**
         * 是否可以切换租户
         */
        String CAN_SWITCH = "can_switch_tenant";
    }

    /** 客户端域 */
    interface Client {
        /**
         * 客户端ID
         */
        String ID = "client_id";
    }

    /** 令牌域 */
    interface Token {
        /**
         * 令牌类型
         */
        String TYPE = "tokenType";
        /**
         * Token 版本号
         * <p>
         * 用于用户级会话失效，当用户修改密码、被禁用、强制下线时递增版本号，
         * 使该用户之前签发的所有 Token 失效。
         */
        String VERSION = "tokenVersion";
        String JTI = "jti";
    }

    /** 会员域 */
    interface Member {
        /**
         * 会员ID
         */
        String ID = "memberId";
    }

}
