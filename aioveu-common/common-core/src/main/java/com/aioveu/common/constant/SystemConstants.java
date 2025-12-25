package com.aioveu.common.constant;


/**
 * @Description: TODO 系统常量
 * @Author: 雒世松
 * @Date: 2025/6/5 15:38
 * @param
 * @return:
 **/

public interface SystemConstants {

    /**
     * 根部门ID
     */
    Long ROOT_NODE_ID = 0L;


    /**
     * 系统默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 超级管理员角色编码
     */
    String ROOT_ROLE_CODE = "ROOT";

    /**
     * 系统配置 IP的QPS限流的KEY
     */
    String SYSTEM_CONFIG_IP_QPS_LIMIT_KEY = "IP_QPS_THRESHOLD_LIMIT";

}
