package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.converter;

import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.entity.Oauth2AuthorizationConsent;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.form.Oauth2AuthorizationConsentForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: Oauth2AuthorizationConsentConverter
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:32
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface Oauth2AuthorizationConsentConverter {

    Oauth2AuthorizationConsentForm toForm(Oauth2AuthorizationConsent entity);

    Oauth2AuthorizationConsent toEntity(Oauth2AuthorizationConsentForm formData);
}
