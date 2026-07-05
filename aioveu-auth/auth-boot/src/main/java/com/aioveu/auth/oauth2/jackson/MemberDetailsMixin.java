package com.aioveu.auth.oauth2.jackson;

import com.fasterxml.jackson.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @ClassName: MemberDetailsMixin
 * @Description TODO  MemberDetails 反序列化注册
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/6 19:53
 * @Version 1.0
 **/


@Slf4j
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class MemberDetailsMixin {




       // 空的 Mixin，依赖字段自动检测 log.info("MemberDetails 反序列化注册");
}
