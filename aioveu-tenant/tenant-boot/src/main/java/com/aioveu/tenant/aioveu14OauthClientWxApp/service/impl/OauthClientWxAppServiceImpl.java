package com.aioveu.tenant.aioveu14OauthClientWxApp.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantVO;
import com.aioveu.tenant.aioveu01Tenant.service.TenantService;
import com.aioveu.tenant.aioveu14OauthClientWxApp.converter.OauthClientWxAppConverter;
import com.aioveu.tenant.aioveu14OauthClientWxApp.mapper.OauthClientWxAppMapper;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.entity.OauthClientWxApp;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.form.OauthClientWxAppForm;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.query.OauthClientWxAppQuery;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.OauthClientWxAppVo;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.TenantWxAppInfo;
import com.aioveu.tenant.aioveu14OauthClientWxApp.service.OauthClientWxAppService;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import com.aioveu.tenant.aioveu15TenantWxApp.service.TenantWxAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: OauthClientWxAppServiceImpl
 * @Description TODO OAuth2客户端与微信小程序映射服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:53
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthClientWxAppServiceImpl extends ServiceImpl<OauthClientWxAppMapper, OauthClientWxApp> implements OauthClientWxAppService {

    private final OauthClientWxAppConverter oauthClientWxAppConverter;

    private final TenantWxAppService tenantWxAppService;

    private final TenantService tenantService;

    /**
     * 获取OAuth2客户端与微信小程序映射分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OauthClientWxAppVo>} OAuth2客户端与微信小程序映射分页列表
     */
    @Override
    public IPage<OauthClientWxAppVo> getOauthClientWxAppPage(OauthClientWxAppQuery queryParams) {
        Page<OauthClientWxAppVo> page = this.baseMapper.getOauthClientWxAppPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2客户端与微信小程序映射表单数据
     *
     * @param id OAuth2客户端与微信小程序映射ID
     * @return OAuth2客户端与微信小程序映射表单数据
     */
    @Override
    public OauthClientWxAppForm getOauthClientWxAppFormData(Long id) {
        OauthClientWxApp entity = this.getById(id);
        return oauthClientWxAppConverter.toForm(entity);
    }

    /**
     * 新增OAuth2客户端与微信小程序映射
     *
     * @param formData OAuth2客户端与微信小程序映射表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOauthClientWxApp(OauthClientWxAppForm formData) {
        OauthClientWxApp entity = oauthClientWxAppConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新OAuth2客户端与微信小程序映射
     *
     * @param id   OAuth2客户端与微信小程序映射ID
     * @param formData OAuth2客户端与微信小程序映射表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOauthClientWxApp(Long id,OauthClientWxAppForm formData) {
        OauthClientWxApp entity = oauthClientWxAppConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2客户端与微信小程序映射
     *
     * @param ids OAuth2客户端与微信小程序映射ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauthClientWxApps(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2客户端与微信小程序映射数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 通过 clientId 获取租户和小程序信息
     *
     * @param clientId
     * @return TenantWxAppInfo
     */
    @Override
    public TenantWxAppInfo getTenantWxAppInfoByClientId(String clientId) {
        // 实现数据库查询
        // 查询SQL示例：
        // SELECT wx_appid, tenant_id FROM mini_program_config WHERE client_id = ?

        // 获取entity
        OauthClientWxApp entity = this.getOne(new LambdaQueryWrapper<OauthClientWxApp>()
                .eq(OauthClientWxApp::getClientId, clientId)
                .select(
                        OauthClientWxApp::getWxAppid,
                        OauthClientWxApp::getWxAppname
                ));
        Assert.isTrue(entity != null, "clientId不存在");

        String wxAppid = entity.getWxAppid();
        String wxAppname = entity.getWxAppname();
        log.info("【Tenant OauthClientWxApp】通过 clientId 获取小程序信息wxAppid:{},wxAppname:{}",wxAppid,wxAppname);

        Long tenantId = tenantWxAppService.getTenantIdByWxAppid(wxAppid);

        TenantWxAppVo tenantWxAppVo = tenantWxAppService.getConfigByWxAppid(wxAppid);

        String appSecret = tenantWxAppVo.getAppSecret();
        log.info("【Tenant OauthClientWxApp】通过 wxAppid 获取小程序配置信息wxAppid:{},appSecret:{}",wxAppid,appSecret);

        TenantVO tenant = tenantService.getTenantById(tenantId);
        String tenantName = tenant.getName();
        log.info("【Tenant OauthClientWxApp】通过 wxAppid 获取租户信息tenantId:{},tenantName:{}",tenantId,tenantName);

        TenantWxAppInfo tenantWxAppInfo =TenantWxAppInfo.builder()
                .clientId(clientId)
                .wxAppid(wxAppid)
                .appSecret(appSecret)
                .tenantId(tenantId)
                .tenantName(tenantName)
                .build();

        log.info("【Tenant OauthClientWxApp】构建租户信息:{}",tenantWxAppInfo);


        return  tenantWxAppInfo;
    }
}
