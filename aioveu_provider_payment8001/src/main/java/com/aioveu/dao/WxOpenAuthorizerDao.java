package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.WxOpenAuthorizer;
import com.aioveu.vo.WxOpenAuthorizerVo;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: luyao
 * @date: 2025/11/17 10:42
 */
@Repository
public interface WxOpenAuthorizerDao extends BaseMapper<WxOpenAuthorizer> {

    /**
     * 查询小程序列表
     * @param page
     * @param authorizerUserId
     * @return
     */
    IPage<WxOpenAuthorizerVo> getListByPage(Page<WxOpenAuthorizer> page, String authorizerUserId);

}
