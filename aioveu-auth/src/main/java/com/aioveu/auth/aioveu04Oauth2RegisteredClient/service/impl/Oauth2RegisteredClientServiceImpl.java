package com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.converter.Oauth2RegisteredClientConverter;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.mapper.Oauth2RegisteredClientMapper;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query.Oauth2RegisteredClientQuery;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.vo.Oauth2RegisteredClientVo;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.Oauth2RegisteredClientService;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: Oauth2RegisteredClientServiceImpl
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:21
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2RegisteredClientServiceImpl extends ServiceImpl<Oauth2RegisteredClientMapper, Oauth2RegisteredClient> implements Oauth2RegisteredClientService {

    private final Oauth2RegisteredClientConverter oauth2RegisteredClientConverter;

    private final JdbcRegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<Oauth2RegisteredClientVo>} OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     */
    @Override
    public IPage<Oauth2RegisteredClientVo> getOauth2RegisteredClientPage(Oauth2RegisteredClientQuery queryParams) {

        // 由于JdbcRegisteredClientRepository不提供分页查询，需要自定义实现
        // 这里使用自定义的Repository或JdbcTemplate实现

        Page<Oauth2RegisteredClientVo> page = this.baseMapper.getOauth2RegisteredClientPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     *
     * @param id OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @return OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     */
    @Override
    public Oauth2RegisteredClientForm getOauth2RegisteredClientFormData(Long id) {
        Oauth2RegisteredClient entity = this.getById(id);
        return oauth2RegisteredClientConverter.toForm(entity);
    }

    /**
     * 新增OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public boolean saveOauth2RegisteredClient(Oauth2RegisteredClientForm formData) {

        // 检查客户端是否已存在
        RegisteredClient existingClient = registeredClientRepository.findByClientId(formData.getClientId());
        if (existingClient != null) {
            throw new BusinessException(ResultCode.CLIENT_ALREADY_EXISTS, formData.getClientId());
        }
        log.info("检查客户端是否已存在: existingClient={}", existingClient);

        // 生成客户端密钥
        String clientSecret = StringUtils.hasText(formData.getClientSecret())
                ? formData.getClientSecret()
                : generateRandomSecret();
        String encodedSecret = passwordEncoder.encode(clientSecret);
        log.info("生成客户端密钥: encodedSecret={}", encodedSecret);

        // 构建RegisteredClient
        RegisteredClient registeredClient = buildRegisteredClient(formData, encodedSecret);


        Oauth2RegisteredClient entity = oauth2RegisteredClientConverter.toEntity(formData);
        // 保存到数据库
        registeredClientRepository.save(registeredClient);

        log.info("客户端注册成功: clientId={}", formData.getClientId());


        return true;  // 不再保存自己的实体
    }

    /*
    * 构建客户端
    * */
    private RegisteredClient buildRegisteredClient(Oauth2RegisteredClientForm formData, String encodedSecret) {

        return buildRegisteredClient(formData, encodedSecret, UUID.randomUUID().toString());
    }

    /*
     * 构建客户端
     * */
    private RegisteredClient buildRegisteredClient(Oauth2RegisteredClientForm formData, String encodedSecret, String id) {

        log.info("生成的随机id: id={}", id);

        RegisteredClient.Builder builder = RegisteredClient.withId(id)
                .clientId(formData.getClientId())
                .clientSecret(encodedSecret)
                .clientName(formData.getClientName());

        // 设置认证方法
        String methods = formData.getClientAuthenticationMethods();
        if (methods != null  && !methods.trim().isEmpty()) {
            Arrays.stream(methods.split(","))
                    .map(String::trim)
                    .map(ClientAuthenticationMethod::new)
                    .forEach(builder::clientAuthenticationMethod);
        }


        // 设置授权类型
        String grantTypes = formData.getAuthorizationGrantTypes();
        if (grantTypes != null && !grantTypes.trim().isEmpty()) {
            // 这里应该是使用 grantTypes 而不是 methods
            Arrays.stream(grantTypes.split(","))
                    .map(String::trim)
                    .map(AuthorizationGrantType::new)
                    .forEach(builder::authorizationGrantType);   // 注意：首字母小写
        }

        // 设置重定向URI
        // 设置重定向URI
        String redirectUris = formData.getRedirectUris();
        if (redirectUris != null && !redirectUris.trim().isEmpty()) {
            Arrays.stream(redirectUris.split(","))
                    .map(String::trim)
                    .forEach(builder::redirectUri);
        }

        // 设置登出重定向URI
        // 设置登出重定向URI
        String logoutUris = formData.getPostLogoutRedirectUris();
        if (logoutUris != null && !logoutUris.trim().isEmpty()) {
            Arrays.stream(logoutUris.split(","))
                    .map(String::trim)
                    .forEach(builder::postLogoutRedirectUri);
        }

        // 设置Scope
        String scopes = formData.getScopes();
        if (scopes != null && !scopes.trim().isEmpty()) {
            Arrays.stream(scopes.split(","))
                    .map(String::trim)
                    .forEach(builder::scope);
        }

        //----------------------------------------------------------------
        // 在业务逻辑中使用 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // 反序列化 clientSettings
        Oauth2RegisteredClientForm.ClientSettingsDto clientSettingsDto = null;
        if (formData.getClientSettings() != null && !formData.getClientSettings().trim().isEmpty()) {
            try {
                clientSettingsDto = objectMapper.readValue(
                        formData.getClientSettings(),
                        Oauth2RegisteredClientForm.ClientSettingsDto.class
                );
            } catch (Exception e) {
                throw new IllegalArgumentException("客户端设置JSON格式错误: " + e.getMessage(), e);
            }
        } else {
            clientSettingsDto = new Oauth2RegisteredClientForm.ClientSettingsDto(); // 使用默认值
        }

        // 反序列化 tokenSettings
        Oauth2RegisteredClientForm.TokenSettingsDto tokenSettingsDto = null;
        if (formData.getTokenSettings() != null && !formData.getTokenSettings().trim().isEmpty()) {
            try {
                tokenSettingsDto = objectMapper.readValue(
                        formData.getTokenSettings(),
                        Oauth2RegisteredClientForm.TokenSettingsDto.class
                );
            } catch (Exception e) {
                throw new IllegalArgumentException("令牌设置JSON格式错误: " + e.getMessage(), e);
            }
        } else {
            tokenSettingsDto = new Oauth2RegisteredClientForm.TokenSettingsDto(); // 使用默认值
        }


        // 客户端设置
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(clientSettingsDto.getRequireAuthorizationConsent())
                .requireProofKey(clientSettingsDto.getRequireProofKey())
                .build();
        builder.clientSettings(clientSettings);

        // 令牌设置
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(tokenSettingsDto.getAccessTokenTimeToLiveSeconds()))
                .refreshTokenTimeToLive(Duration.ofSeconds(tokenSettingsDto.getRefreshTokenTimeToLiveSeconds()))
                .reuseRefreshTokens(true)  // 可以放在 DTO 中配置
                .build();
        builder.tokenSettings(tokenSettings);

        return builder.build();
    }


    private String generateRandomSecret() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 更新OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param clientId   OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public boolean updateOauth2RegisteredClient(String clientId,Oauth2RegisteredClientForm formData) {

        RegisteredClient existingClient = getRegisteredClient(clientId);

        // 检查是否修改了clientId
        if (!existingClient.getClientId().equals(formData.getClientId())) {
            // 检查新clientId是否已被占用
            RegisteredClient clientWithNewId = registeredClientRepository.findByClientId(formData.getClientId());
            if (clientWithNewId != null && !clientWithNewId.getId().equals(existingClient.getId())) {
                throw new BusinessException(ResultCode.CLIENT_ALREADY_EXISTS, formData.getClientId());
            }
        }

        // 重新构建客户端
        String clientSecret = null;
        if (StringUtils.hasText(formData.getClientSecret())) {
            clientSecret = formData.getClientSecret();
        }
        String encodedSecret = clientSecret != null
                ? passwordEncoder.encode(clientSecret)
                : existingClient.getClientSecret();

        RegisteredClient updatedClient = buildRegisteredClient(formData, encodedSecret, existingClient.getId());

        Oauth2RegisteredClient entity = oauth2RegisteredClientConverter.toEntity(formData);
        log.info("客户端更新成功: clientId={}", clientId);
        // 保存更新
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param ids OAuth2注册客户端，存储所有已注册的客户端应用信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteOauth2RegisteredClients(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2注册客户端，存储所有已注册的客户端应用信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .map(Long::parseLong)
                .toList();

        // 验证是否有有效的ID
        Assert.isTrue(!idList.isEmpty(), "没有有效的客户端ID");

        // 方式2：使用MyBatis Plus的removeByIds删除
        boolean result = this.removeByIds(idList);

        if (result) {
            log.info("客户端删除成功: ids={}", ids);
        } else {
            log.warn("客户端删除失败: ids={}", ids);
        }

        return this.removeByIds(idList);
    }

    /*
     * 根据ID获取客户端
     * */
    @Override
    public Oauth2RegisteredClientVo getClient( String clientId) {
        RegisteredClient client = getRegisteredClient(clientId);

        return convertToVO(client, null);
    }

    // 私有辅助方法
    private RegisteredClient getRegisteredClient(String clientId) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            throw new BusinessException(ResultCode.CLIENT_NOT_FOUND, clientId);
        }
        return client;
    }

    private Oauth2RegisteredClientVo convertToVO(RegisteredClient client, String rawSecret) {
        Oauth2RegisteredClientVo oauth2RegisteredClient = new Oauth2RegisteredClientVo();
        oauth2RegisteredClient.setId(client.getId());
        oauth2RegisteredClient.setClientId(client.getClientId());
        oauth2RegisteredClient.setClientName(client.getClientName());
        oauth2RegisteredClient.setClientSecret(rawSecret);
        oauth2RegisteredClient.setClientIdIssuedAt(LocalDateTime.now());
        oauth2RegisteredClient.setEnabled(true);
        return oauth2RegisteredClient;
    }

    /*
     * 重置客户端密钥
     * */
    @Override
    public Oauth2RegisteredClientVo resetClientSecret(String clientId) {
        RegisteredClient client = getRegisteredClient(clientId);

        // 生成新密钥
        String newSecret = generateRandomSecret();
        String encodedSecret = passwordEncoder.encode(newSecret);

        // 更新客户端密钥
        RegisteredClient updatedClient = RegisteredClient.from(client)
                .clientSecret(encodedSecret)
                .build();

        registeredClientRepository.save(updatedClient);

        log.info("客户端密钥重置成功: clientId={}", clientId);

        return convertToVO(updatedClient, newSecret);


    }

    /*
     * 修改客户端状态
     * */
    @Override
    public void toggleClientStatus(String clientId, boolean enabled) {
        // 这里需要扩展RegisteredClient来支持启用/禁用状态
        // 实际实现中可能需要添加自定义字段
        log.info("修改客户端状态: clientId={}, enabled={}", clientId, enabled);
        // 实现细节根据业务需求决定

    }

}
