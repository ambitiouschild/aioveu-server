package com.aioveu.pms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.mapper.PmsCategoryAttributeMapper;
import com.aioveu.pms.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO 商品属性业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/

@Service
public class AttributeServiceImpl extends ServiceImpl<PmsCategoryAttributeMapper, PmsCategoryAttribute> implements AttributeService {

    /**
     * 批量保存商品属性
     *
     * @param formData 表单数据
     * @return
     */
    @Override
    public boolean saveBatch(PmsCategoryAttributeForm formData) {
        Long categoryId = formData.getCategoryId();
        Integer attributeType = formData.getType();

        List<Long> formIds = formData.getAttributes().stream()
                .filter(item -> item.getId() != null)
                .map(item -> item.getId())
                .collect(Collectors.toList());

        List<Long> dbIds = this.list(new LambdaQueryWrapper<PmsCategoryAttribute>()
                .eq(PmsCategoryAttribute::getCategoryId, categoryId)
                .eq(PmsCategoryAttribute::getType, attributeType)
                .select(PmsCategoryAttribute::getId)).stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());

        // 删除此次表单没有的属性ID
        if (CollectionUtil.isNotEmpty(dbIds)) {
            List<Long> rmIds = dbIds.stream()
                    .filter(id -> CollectionUtil.isEmpty(formIds) || !formIds.contains(id))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(rmIds)) {
                this.removeByIds(rmIds);
            }
        }

        // 新增/修改表单提交的属性
        List<PmsCategoryAttributeForm.Attribute> formAttributes = formData.getAttributes();

        List<PmsCategoryAttribute> attributeList = new ArrayList<>();

        formAttributes.forEach(item -> {
            PmsCategoryAttribute attribute = PmsCategoryAttribute.builder().id(item.getId()).categoryId(categoryId).type(attributeType).name(item.getName()).build();
            attributeList.add(attribute);
        });
        boolean result = this.saveOrUpdateBatch(attributeList);
        return result;
    }
}
