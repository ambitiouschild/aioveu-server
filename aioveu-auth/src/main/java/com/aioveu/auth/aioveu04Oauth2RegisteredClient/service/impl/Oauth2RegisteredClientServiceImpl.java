package com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.converter.Oauth2RegisteredClientConverter;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.mapper.Oauth2RegisteredClientMapper;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query.Oauth2RegisteredClientQuery;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.vo.Oauth2RegisteredClientVo;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.Oauth2RegisteredClientService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: Oauth2RegisteredClientServiceImpl
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:21
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class Oauth2RegisteredClientServiceImpl extends ServiceImpl<Oauth2RegisteredClientMapper, Oauth2RegisteredClient> implements Oauth2RegisteredClientService {

    private final Oauth2RegisteredClientConverter oauth2RegisteredClientConverter;

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<Oauth2RegisteredClientVo>} OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     */
    @Override
    public IPage<Oauth2RegisteredClientVo> getOauth2RegisteredClientPage(Oauth2RegisteredClientQuery queryParams) {
        Page<Oauth2RegisteredClientVo> page = this.baseMapper.getOauth2RegisteredClientPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     *
     * @param id OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @return OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     */
    @Override
    public Oauth2RegisteredClientForm getOauth2RegisteredClientFormData(Long id) {
        Oauth2RegisteredClient entity = this.getById(id);
        return oauth2RegisteredClientConverter.toForm(entity);
    }

    /**
     * 新增OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOauth2RegisteredClient(Oauth2RegisteredClientForm formData) {
        Oauth2RegisteredClient entity = oauth2RegisteredClientConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param id   OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOauth2RegisteredClient(Long id,Oauth2RegisteredClientForm formData) {
        Oauth2RegisteredClient entity = oauth2RegisteredClientConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param ids OAuth2注册客户端，存储所有已注册的客户端应用信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOauth2RegisteredClients(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的OAuth2注册客户端，存储所有已注册的客户端应用信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
