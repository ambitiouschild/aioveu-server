package com.aioveu.oms.aioveu10MqConsumeIdempotent.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.converter.MqConsumeIdempotentConverter;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.mapper.MqConsumeIdempotentMapper;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.entity.MqConsumeIdempotent;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.form.MqConsumeIdempotentForm;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.query.MqConsumeIdempotentQuery;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.vo.MqConsumeIdempotentVo;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.service.MqConsumeIdempotentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: MqConsumeIdempotentServiceImpl
 * @Description TODO MQ消费幂等性服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:21
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class MqConsumeIdempotentServiceImpl extends ServiceImpl<MqConsumeIdempotentMapper, MqConsumeIdempotent> implements MqConsumeIdempotentService {

    private final MqConsumeIdempotentConverter mqConsumeIdempotentConverter;

    /**
     * 获取MQ消费幂等性分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqConsumeIdempotentVo>} MQ消费幂等性分页列表
     */
    @Override
    public IPage<MqConsumeIdempotentVo> getMqConsumeIdempotentPage(MqConsumeIdempotentQuery queryParams) {
        Page<MqConsumeIdempotentVo> page = this.baseMapper.getMqConsumeIdempotentPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ消费幂等性表单数据
     *
     * @param id MQ消费幂等性ID
     * @return MQ消费幂等性表单数据
     */
    @Override
    public MqConsumeIdempotentForm getMqConsumeIdempotentFormData(Long id) {
        MqConsumeIdempotent entity = this.getById(id);
        return mqConsumeIdempotentConverter.toForm(entity);
    }

    /**
     * 新增MQ消费幂等性
     *
     * @param formData MQ消费幂等性表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqConsumeIdempotent(MqConsumeIdempotentForm formData) {
        MqConsumeIdempotent entity = mqConsumeIdempotentConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ消费幂等性
     *
     * @param id   MQ消费幂等性ID
     * @param formData MQ消费幂等性表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqConsumeIdempotent(Long id,MqConsumeIdempotentForm formData) {
        MqConsumeIdempotent entity = mqConsumeIdempotentConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ消费幂等性
     *
     * @param ids MQ消费幂等性ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqConsumeIdempotents(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ消费幂等性数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
