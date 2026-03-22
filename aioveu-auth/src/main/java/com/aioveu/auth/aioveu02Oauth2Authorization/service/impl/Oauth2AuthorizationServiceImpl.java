package com.aioveu.auth.aioveu02Oauth2Authorization.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.aioveu02Oauth2Authorization.converter.Oauth2AuthorizationConverter;
import com.aioveu.auth.aioveu02Oauth2Authorization.mapper.Oauth2AuthorizationMapper;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.entity.Oauth2Authorization;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.form.Oauth2AuthorizationForm;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.query.Oauth2AuthorizationQuery;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.vo.Oauth2AuthorizationVo;
import com.aioveu.auth.aioveu02Oauth2Authorization.service.Oauth2AuthorizationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: Oauth2AuthorizationServiceImpl
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:01
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class Oauth2AuthorizationServiceImpl extends ServiceImpl<Oauth2AuthorizationMapper, Oauth2Authorization> implements Oauth2AuthorizationService {

    private final Oauth2AuthorizationConverter oauth2AuthorizationConverter;

    /**
     * 获取OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<Oauth2AuthorizationVo>} OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表
     */
    @Override
    public IPage<Oauth2AuthorizationVo> getOauth2AuthorizationPage(Oauth2AuthorizationQuery queryParams) {
        Page<Oauth2AuthorizationVo> page = this.baseMapper.getOauth2AuthorizationPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单数据
     *
     * @param id OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID
     * @return OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单数据
     */
    @Override
    public Oauth2AuthorizationForm getOauth2AuthorizationFormData(Long id) {
        Oauth2Authorization entity = this.getById(id);
        return oauth2AuthorizationConverter.toForm(entity);
    }

    /**
     * 新增OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param formData OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOauth2Authorization(Oauth2AuthorizationForm formData) {
        Oauth2Authorization entity = oauth2AuthorizationConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param id   OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID
     * @param formData OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOauth2Authorization(Long id,Oauth2AuthorizationForm formData) {
        Oauth2Authorization entity = oauth2AuthorizationConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param ids OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauth2Authorizations(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2授权信息，存储所有的授权记录、令牌和状态信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



}
