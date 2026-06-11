package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.mapper;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.entity.Oauth2RegisteredClientBiz;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.query.Oauth2RegisteredClientBizQuery;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.vo.Oauth2RegisteredClientBizVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: Oauth2RegisteredClientBizMapper
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:33
 * @Version 1.0
 **/
@Mapper
public interface Oauth2RegisteredClientBizMapper extends BaseMapper<Oauth2RegisteredClientBiz> {

    /**
     * 获取OAuth2 客户端业务状态（auth 服务本地校验用）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<Oauth2RegisteredClientBizVo>} OAuth2 客户端业务状态（auth 服务本地校验用）分页列表
     */
    Page<Oauth2RegisteredClientBizVo> getOauth2RegisteredClientBizPage(Page<Oauth2RegisteredClientBizVo> page, Oauth2RegisteredClientBizQuery queryParams);


    /**
     * 根据 clientId 查询业务状态
     */
    Oauth2RegisteredClientBiz selectById(@Param("clientId") String clientId);

}
