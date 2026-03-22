package com.aioveu.auth.aioveu04Oauth2RegisteredClient.mapper;

import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query.Oauth2RegisteredClientQuery;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.vo.Oauth2RegisteredClientVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: Oauth2RegisteredClientMapper
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:11
 * @Version 1.0
 **/
@Mapper
public interface Oauth2RegisteredClientMapper extends BaseMapper<Oauth2RegisteredClient> {

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<Oauth2RegisteredClientVo>} OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     */
    Page<Oauth2RegisteredClientVo> getOauth2RegisteredClientPage(Page<Oauth2RegisteredClientVo> page, Oauth2RegisteredClientQuery queryParams);
}
