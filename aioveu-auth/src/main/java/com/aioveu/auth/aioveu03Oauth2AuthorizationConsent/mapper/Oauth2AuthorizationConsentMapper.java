package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.mapper;

import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.entity.Oauth2AuthorizationConsent;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.query.Oauth2AuthorizationConsentQuery;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.vo.Oauth2AuthorizationConsentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: Oauth2AuthorizationConsentMapper
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:29
 * @Version 1.0
 **/
@Mapper
public interface Oauth2AuthorizationConsentMapper extends BaseMapper<Oauth2AuthorizationConsent> {

    /**
     * 获取OAuth2授权同意，记录用户对每个客户端的授权同意情况分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<Oauth2AuthorizationConsentVo>} OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表
     */
    Page<Oauth2AuthorizationConsentVo> getOauth2AuthorizationConsentPage(Page<Oauth2AuthorizationConsentVo> page, Oauth2AuthorizationConsentQuery queryParams);
}
