package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.converter.Oauth2RegisteredClientBizConverter;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.mapper.Oauth2RegisteredClientBizMapper;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.entity.Oauth2RegisteredClientBiz;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.form.Oauth2RegisteredClientBizForm;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.query.Oauth2RegisteredClientBizQuery;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.vo.Oauth2RegisteredClientBizVo;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.service.Oauth2RegisteredClientBizService;
import com.aioveu.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: Oauth2RegisteredClientBizServiceImpl
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:36
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2RegisteredClientBizServiceImpl extends ServiceImpl<Oauth2RegisteredClientBizMapper, Oauth2RegisteredClientBiz> implements Oauth2RegisteredClientBizService {


    private final Oauth2RegisteredClientBizConverter oauth2RegisteredClientBizConverter;

    private final RedisTemplate<String, Object> redisTemplate;
    //Redis 缓存（防止高频打库） 进阶优化（强烈推荐）
    private static final String CACHE_KEY = "CLIENT_WHITELIST";
    private static final long CACHE_TTL = 30; // 分钟

    /**
     * 获取OAuth2 客户端业务状态（auth 服务本地校验用）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<Oauth2RegisteredClientBizVo>} OAuth2 客户端业务状态（auth 服务本地校验用）分页列表
     */
    @Override
    public IPage<Oauth2RegisteredClientBizVo> getOauth2RegisteredClientBizPage(Oauth2RegisteredClientBizQuery queryParams) {
        Page<Oauth2RegisteredClientBizVo> page = this.baseMapper.getOauth2RegisteredClientBizPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2 客户端业务状态（auth 服务本地校验用）表单数据
     *
     * @param id OAuth2 客户端业务状态（auth 服务本地校验用）ID
     * @return OAuth2 客户端业务状态（auth 服务本地校验用）表单数据
     */
    @Override
    public Oauth2RegisteredClientBizForm getOauth2RegisteredClientBizFormData(Long id) {
        Oauth2RegisteredClientBiz entity = this.getById(id);
        return oauth2RegisteredClientBizConverter.toForm(entity);
    }

    /**
     * 新增OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param formData OAuth2 客户端业务状态（auth 服务本地校验用）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOauth2RegisteredClientBiz(Oauth2RegisteredClientBizForm formData) {
        Oauth2RegisteredClientBiz entity = oauth2RegisteredClientBizConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param id   OAuth2 客户端业务状态（auth 服务本地校验用）ID
     * @param formData OAuth2 客户端业务状态（auth 服务本地校验用）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOauth2RegisteredClientBiz(Long id,Oauth2RegisteredClientBizForm formData) {
        Oauth2RegisteredClientBiz entity = oauth2RegisteredClientBizConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param ids OAuth2 客户端业务状态（auth 服务本地校验用）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauth2RegisteredClientBizs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2 客户端业务状态（auth 服务本地校验用）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



    /*
     * 禁用客户端
     * */
    @Transactional
    @Override
    public void disableClient(String clientId) {

        // 1️改 DB（禁用）
        int rows = this.baseMapper.disable(clientId);

        if (rows == 0) {
            throw new BusinessException("客户端不存在或已禁用");
        }

        // 2️✅ 同步删除 Redis 白名单
        Long removed = redisTemplate.opsForSet()
                .remove(CACHE_KEY, clientId);

        log.info("【Auth】禁用 clientId={}，DB影响行数={}，Redis移除={}",
                clientId, rows, removed);
    }


    /*
     * 开启客户端
     * */
    @Transactional
    @Override
    public void enableClient(String clientId) {

        // 1️改 DB（启用）
        this.baseMapper.enable(clientId);

        // 2️✅ 同步加入 Redis 白名单
        redisTemplate.opsForSet()
                .add(CACHE_KEY, clientId);
        redisTemplate.expire(
                CACHE_KEY,
                CACHE_TTL,
                TimeUnit.MINUTES
        );

        log.info("【Auth】启用 clientId={}，已加入 Redis 白名单", clientId);
    }

}
