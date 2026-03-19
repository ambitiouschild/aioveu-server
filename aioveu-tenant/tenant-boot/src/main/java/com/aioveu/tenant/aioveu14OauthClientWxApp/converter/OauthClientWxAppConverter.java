package com.aioveu.tenant.aioveu14OauthClientWxApp.converter;

import com.aioveu.tenant.aioveu14OauthClientWxApp.model.entity.OauthClientWxApp;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.form.OauthClientWxAppForm;
import org.mapstruct.Mapper;


/**
 * @ClassName: OauthClientWxAppConverter
 * @Description TODO OAuth2客户端与微信小程序映射对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:51
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface OauthClientWxAppConverter {

    OauthClientWxAppForm toForm(OauthClientWxApp entity);

    OauthClientWxApp toEntity(OauthClientWxAppForm formData);
}
