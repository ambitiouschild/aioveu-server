package com.aioveu.pms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.mapper.PmsCategoryAttributeMapper;
import com.aioveu.pms.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.service.AttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO 商品属性业务实现类
 *                      详细注释说明：
 *                       1.方法功能概述：
 *                          实现商品属性的批量保存，支持新增、修改、删除三种操作
 *                          通过对比表单ID列表和数据库ID列表，智能识别需要进行的操作
 *                       2.核心处理逻辑：
 *                          删除操作：找出数据库中存在但表单中不存在的属性ID，进行删除
 *                          新增/更新操作：将表单中的所有属性转换为实体对象，批量保存或更新
 *                       3.关键技术点：
 *                          使用Stream API进行集合操作和数据处理
 *                          使用MyBatis-Plus的LambdaQueryWrapper构建条件查询
 *                          使用Hutool的CollectionUtil进行集合空值判断
 *                          利用MyBatis-Plus的saveOrUpdateBatch实现智能保存
 *                       4.业务逻辑说明：
 *                          属性ID为null表示新增属性
 *                          属性ID不为null且在表单中存在表示需要更新
 *                          属性ID在数据库中存在但在表单中不存在表示需要删除
 *                       5.性能优化：
 *                          查询时使用select只获取ID字段，减少数据传输
 *                          使用批量操作减少数据库交互次数
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/
@Slf4j
@Service
public class AttributeServiceImpl extends ServiceImpl<PmsCategoryAttributeMapper, PmsCategoryAttribute> implements AttributeService {

    /**
     *    TODO              批量保存商品属性（包含新增、修改、删除操作）
     *                  核心逻辑：对比表单提交的属性ID列表和数据库中现有属性ID列表，进行差异化的增删改操作
     *
     * @param formData 商品属性表单数据，包含分类ID、属性类型和属性列表
     * @return boolean 操作是否成功
     */
    @Override
    public boolean saveBatch(PmsCategoryAttributeForm formData) {

        log.info("从表单数据中获取分类ID和属性类型（1-规格属性，2-参数属性等）");
        Long categoryId = formData.getCategoryId();
        Integer attributeType = formData.getType();

        log.info("从表单提交的属性列表中提取所有非空的属性ID");
        log.info("这些ID代表前端希望保留的属性（可能是已存在需要更新的，也可能是新添加的）");
        List<Long> formIds = formData.getAttributes().stream()
                .filter(item -> item.getId() != null)   // 过滤掉ID为null的新增属性
                .map(item -> item.getId()) // 提取属性ID
                .collect(Collectors.toList());   // 收集到List中

        List<Long> dbIds = this.list(new LambdaQueryWrapper<PmsCategoryAttribute>()
                .eq(PmsCategoryAttribute::getCategoryId, categoryId)    // 条件：分类ID相等
                .eq(PmsCategoryAttribute::getType, attributeType)      // 条件：属性类型相等
                .select(PmsCategoryAttribute::getId))    // 只查询ID字段，提高效率
                .stream()
                .map(item -> item.getId())  // 提取数据库中的属性ID
                .collect(Collectors.toList());  // 收集到List中

        // 删除此次表单没有的属性ID
        log.info("删除此次表单没有提交的属性ID（即需要删除的属性）");
        if (CollectionUtil.isNotEmpty(dbIds)) {

            log.info("找出需要删除的属性ID：在数据库中存在但在表单中不存在的ID");
            List<Long> rmIds = dbIds.stream()
                    .filter(id -> CollectionUtil.isEmpty(formIds) || !formIds.contains(id))
                    // 如果表单ID列表为空，则删除所有现有属性
                    // 如果表单ID列表不为空，则删除表单中不包含的数据库ID
                    .collect(Collectors.toList());

            log.info("如果存在需要删除的属性，执行批量删除");
            if (CollectionUtil.isNotEmpty(rmIds)) {
                this.removeByIds(rmIds);
            }
        }

        // 新增/修改表单提交的属性
        log.info("处理新增和修改的表单属性数据");
        List<PmsCategoryAttributeForm.Attribute> formAttributes = formData.getAttributes();

        log.info("创建属性实体列表，用于批量保存或更新");
        List<PmsCategoryAttribute> attributeList = new ArrayList<>();

        log.info("遍历表单中的每个属性，转换为实体对象");
        formAttributes.forEach(item -> {

            // 使用建造者模式创建属性实体
            PmsCategoryAttribute attribute = PmsCategoryAttribute.builder()
                    .id(item.getId())   // 属性ID：新增时为null，更新时不为null
                    .categoryId(categoryId)  // 分类ID
                    .type(attributeType)  // 属性类型
                    .name(item.getName())  // 属性名称
                    .build();
            attributeList.add(attribute);
        });

        log.info("批量保存或更新属性列表");
        log.info("saveOrUpdateBatch方法会自动判断：ID为null执行新增，ID不为null执行更新");
        boolean result = this.saveOrUpdateBatch(attributeList);
        return result;
    }
}
