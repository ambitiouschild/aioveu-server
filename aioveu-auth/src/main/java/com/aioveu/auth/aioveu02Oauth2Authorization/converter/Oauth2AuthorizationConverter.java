package com.aioveu.auth.aioveu02Oauth2Authorization.converter;

import com.aioveu.auth.aioveu02Oauth2Authorization.model.entity.Oauth2Authorization;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.form.Oauth2AuthorizationForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: Oauth2AuthorizationConverter
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:00
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface Oauth2AuthorizationConverter {

    Oauth2AuthorizationForm toForm(Oauth2Authorization entity);

    Oauth2Authorization toEntity(Oauth2AuthorizationForm formData);
}
