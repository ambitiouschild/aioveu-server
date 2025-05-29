package com.aioveu.auth.common.security.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.model.RequestConstant;
import com.aioveu.auth.common.model.TokenConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 雒世松
 * 微服务过滤器，解密网关传递的用户信息，将其放入request中，便于后期业务方法直接获取用户信息
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    /**
     * 具体方法主要分为两步
     * 1. 解密网关传递的信息
     * 2. 将解密之后的信息封装放入到request中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的加密的用户信息
        String token = request.getHeader(TokenConstant.TOKEN_NAME);
        if (StrUtil.isNotBlank(token)){
            //解密
            String json = Base64.decodeStr(token);
            JSONObject jsonObject = JSON.parseObject(json);
            //获取用户身份信息、权限信息
            String principal = jsonObject.getString(TokenConstant.PRINCIPAL_NAME);
            String userId=jsonObject.getString(TokenConstant.USER_ID);
            String jti = jsonObject.getString(TokenConstant.JTI);
            Long expireIn = jsonObject.getLong(TokenConstant.EXPR);
            String nickName = jsonObject.getString(TokenConstant.NICK_NAME);
            Integer gender = jsonObject.getInteger(TokenConstant.GENDER);
            String email = jsonObject.getString(TokenConstant.EMAIL);
            String mobile = jsonObject.getString(TokenConstant.MOBILE);
            String avatar = jsonObject.getString(TokenConstant.AVATAR);
            JSONArray tempJsonArray = jsonObject.getJSONArray(TokenConstant.AUTHORITIES_NAME);
            //权限
            String[] authorities =  tempJsonArray.toArray(new String[0]);
            //放入LoginVal
            LoginVal loginVal = new LoginVal();
            loginVal.setUserId(userId);
            loginVal.setUsername(principal);
            loginVal.setNickname(nickName);
            loginVal.setEmail(email);
            loginVal.setHead(avatar);
            loginVal.setGender(gender);
            loginVal.setPhone(mobile);
            loginVal.setAuthorities(authorities);
            loginVal.setJti(jti);
            loginVal.setExpireIn(expireIn);
            //放入request的attribute中
            request.setAttribute(RequestConstant.LOGIN_VAL_ATTRIBUTE,loginVal);
        }
        filterChain.doFilter(request,response);
    }
}