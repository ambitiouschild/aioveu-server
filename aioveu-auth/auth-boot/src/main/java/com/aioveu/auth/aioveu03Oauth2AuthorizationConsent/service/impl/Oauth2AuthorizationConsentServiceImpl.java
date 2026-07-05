package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.converter.Oauth2AuthorizationConsentConverter;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.mapper.Oauth2AuthorizationConsentMapper;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.entity.Oauth2AuthorizationConsent;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.form.Oauth2AuthorizationConsentForm;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.query.Oauth2AuthorizationConsentQuery;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.vo.Oauth2AuthorizationConsentVo;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.service.Oauth2AuthorizationConsentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: Oauth2AuthorizationConsentServiceImpl
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:30
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class Oauth2AuthorizationConsentServiceImpl extends ServiceImpl<Oauth2AuthorizationConsentMapper, Oauth2AuthorizationConsent> implements Oauth2AuthorizationConsentService {

    private final Oauth2AuthorizationConsentConverter oauth2AuthorizationConsentConverter;

    /**
     * 获取OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<Oauth2AuthorizationConsentVo>} OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表
     */
    @Override
    public IPage<Oauth2AuthorizationConsentVo> getOauth2AuthorizationConsentPage(Oauth2AuthorizationConsentQuery queryParams) {
        Page<Oauth2AuthorizationConsentVo> page = this.baseMapper.getOauth2AuthorizationConsentPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2授权同意，记录用户对每个客户端的授权同意情况表单数据
     *
     * @param id OAuth2授权同意，记录用户对每个客户端的授权同意情况ID
     * @return OAuth2授权同意，记录用户对每个客户端的授权同意情况表单数据
     */
    @Override
    public Oauth2AuthorizationConsentForm getOauth2AuthorizationConsentFormData(Long id) {
        Oauth2AuthorizationConsent entity = this.getById(id);
        return oauth2AuthorizationConsentConverter.toForm(entity);
    }

    /**
     * 新增OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param formData OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOauth2AuthorizationConsent(Oauth2AuthorizationConsentForm formData) {
        Oauth2AuthorizationConsent entity = oauth2AuthorizationConsentConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param id   OAuth2授权同意，记录用户对每个客户端的授权同意情况ID
     * @param formData OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOauth2AuthorizationConsent(Long id,Oauth2AuthorizationConsentForm formData) {
        Oauth2AuthorizationConsent entity = oauth2AuthorizationConsentConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param ids OAuth2授权同意，记录用户对每个客户端的授权同意情况ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauth2AuthorizationConsents(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2授权同意，记录用户对每个客户端的授权同意情况数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
