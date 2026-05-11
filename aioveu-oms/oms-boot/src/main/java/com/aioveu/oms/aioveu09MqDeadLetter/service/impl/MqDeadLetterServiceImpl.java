package com.aioveu.oms.aioveu09MqDeadLetter.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu09MqDeadLetter.converter.MqDeadLetterConverter;
import com.aioveu.oms.aioveu09MqDeadLetter.mapper.MqDeadLetterMapper;
import com.aioveu.oms.aioveu09MqDeadLetter.model.entity.MqDeadLetter;
import com.aioveu.oms.aioveu09MqDeadLetter.model.form.MqDeadLetterForm;
import com.aioveu.oms.aioveu09MqDeadLetter.model.query.MqDeadLetterQuery;
import com.aioveu.oms.aioveu09MqDeadLetter.model.vo.MqDeadLetterVo;
import com.aioveu.oms.aioveu09MqDeadLetter.service.MqDeadLetterService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: MqDeadLetterServiceImpl
 * @Description TODO MQ死信队列服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:55
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class MqDeadLetterServiceImpl extends ServiceImpl<MqDeadLetterMapper, MqDeadLetter> implements MqDeadLetterService {

    private final MqDeadLetterConverter mqDeadLetterConverter;

    /**
     * 获取MQ死信队列分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqDeadLetterVo>} MQ死信队列分页列表
     */
    @Override
    public IPage<MqDeadLetterVo> getMqDeadLetterPage(MqDeadLetterQuery queryParams) {
        Page<MqDeadLetterVo> page = this.baseMapper.getMqDeadLetterPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ死信队列表单数据
     *
     * @param id MQ死信队列ID
     * @return MQ死信队列表单数据
     */
    @Override
    public MqDeadLetterForm getMqDeadLetterFormData(Long id) {
        MqDeadLetter entity = this.getById(id);
        return mqDeadLetterConverter.toForm(entity);
    }

    /**
     * 新增MQ死信队列
     *
     * @param formData MQ死信队列表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqDeadLetter(MqDeadLetterForm formData) {
        MqDeadLetter entity = mqDeadLetterConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ死信队列
     *
     * @param id   MQ死信队列ID
     * @param formData MQ死信队列表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqDeadLetter(Long id,MqDeadLetterForm formData) {
        MqDeadLetter entity = mqDeadLetterConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ死信队列
     *
     * @param ids MQ死信队列ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqDeadLetters(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ死信队列数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
