package com.aioveu.tenant.aioveu14OauthClientWxApp.mapper;

import com.aioveu.tenant.aioveu14OauthClientWxApp.model.entity.OauthClientWxApp;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.query.OauthClientWxAppQuery;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.OauthClientWxAppVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: OauthClientWxAppMapper
 * @Description TODO OAuth2客户端与微信小程序映射Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:49
 * @Version 1.0
 **/
@Mapper
public interface OauthClientWxAppMapper extends BaseMapper<OauthClientWxApp> {

    /**
     * 获取OAuth2客户端与微信小程序映射分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OauthClientWxAppVo>} OAuth2客户端与微信小程序映射分页列表
     */
    Page<OauthClientWxAppVo> getOauthClientWxAppPage(Page<OauthClientWxAppVo> page, OauthClientWxAppQuery queryParams);
}
