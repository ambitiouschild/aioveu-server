package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.converter;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.entity.Oauth2RegisteredClientBiz;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.form.Oauth2RegisteredClientBizForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: Oauth2RegisteredClientBizConverter
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:35
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface Oauth2RegisteredClientBizConverter {

    Oauth2RegisteredClientBizForm toForm(Oauth2RegisteredClientBiz entity);

    Oauth2RegisteredClientBiz toEntity(Oauth2RegisteredClientBizForm formData);
}
