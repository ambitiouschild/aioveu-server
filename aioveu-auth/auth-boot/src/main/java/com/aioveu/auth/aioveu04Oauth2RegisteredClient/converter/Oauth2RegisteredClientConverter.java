package com.aioveu.auth.aioveu04Oauth2RegisteredClient.converter;

import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: Oauth2RegisteredClientConverter
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:13
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface Oauth2RegisteredClientConverter {

    Oauth2RegisteredClientForm toForm(Oauth2RegisteredClient entity);

    Oauth2RegisteredClient toEntity(Oauth2RegisteredClientForm formData);
}
