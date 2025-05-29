package com.aioveu.auth.common.utils;


import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.model.RequestConstant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author  雒世松
 * OAuth2.0工具类，从请求的线程中获取个人信息
 */
public class OauthUtils {

    /**
     * 获取当前请求登录用户的详细信息
     */
    public static LoginVal getCurrentUser(){
        return (LoginVal) RequestContextUtils.getRequest().getAttribute(RequestConstant.LOGIN_VAL_ATTRIBUTE);
    }

    public static void setCurrentUser(LoginVal loginVal){
        RequestContextUtils.getRequest().setAttribute(RequestConstant.LOGIN_VAL_ATTRIBUTE, loginVal);
    }

    public static String getCurrentUserId(){
        LoginVal loginVal = getCurrentUser();
        if (loginVal != null) {
            return loginVal.getUserId();
        }
        return null;
    }

    public static String getCurrentUsername(){
        LoginVal loginVal = getCurrentUser();
        if (loginVal != null) {
            return loginVal.getUsername();
        }
        return null;
    }

    public static String getCurrentNickname(){
        LoginVal loginVal = getCurrentUser();
        if (loginVal != null) {
            return loginVal.getNickname();
        }
        return null;
    }

    public static List<String> getCurrentUserRoles(){
        LoginVal loginVal = getCurrentUser();
        if (loginVal != null) {
            return Arrays.asList(loginVal.getAuthorities());
        }
        return Collections.emptyList();
    }

    /**
     * 是否是admin的管理员
     * @return
     */
    public static boolean isAdmin() {
        List<String> currentUserRoles = getCurrentUserRoles();
        if (currentUserRoles != null && !currentUserRoles.isEmpty()) {
            return currentUserRoles.stream().anyMatch(s -> s.equals("admin_advanced"));
        }
        return false;
    }

    /**
     * 仅仅是教练
     * @return
     */
    public static boolean isOnlyTeacher() {
        List<String> currentUserRoles = getCurrentUserRoles();
        if (currentUserRoles != null && !currentUserRoles.isEmpty()) {
            return !isAdmin() && currentUserRoles.stream().anyMatch(s -> s.equals("teacher"));
        }
        return false;
    }

    /**
     * 是否是超级管理员
     * @return
     */
    public static boolean isSuperAdmin() {
        List<String> currentUserRoles = getCurrentUserRoles();
        if (currentUserRoles != null && !currentUserRoles.isEmpty()) {
            return currentUserRoles.stream().anyMatch(s -> s.equals("ROOT"));
        }
        return false;
    }

}
