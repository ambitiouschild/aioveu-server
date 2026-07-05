package com.aioveu.auth.aioveu02Oauth2Authorization.mapper;

import com.aioveu.auth.aioveu02Oauth2Authorization.model.entity.Oauth2Authorization;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.query.Oauth2AuthorizationQuery;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.vo.Oauth2AuthorizationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: Oauth2AuthorizationMapper
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 13:58
 * @Version 1.0
 **/
@Mapper
public interface Oauth2AuthorizationMapper extends BaseMapper<Oauth2Authorization> {

    /**
     * 获取OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<Oauth2AuthorizationVo>} OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表
     */
    Page<Oauth2AuthorizationVo> getOauth2AuthorizationPage(Page<Oauth2AuthorizationVo> page, Oauth2AuthorizationQuery queryParams);
}
