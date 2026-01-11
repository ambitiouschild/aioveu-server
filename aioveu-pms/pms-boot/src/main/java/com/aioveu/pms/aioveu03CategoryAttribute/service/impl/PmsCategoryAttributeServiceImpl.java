package com.aioveu.pms.aioveu03CategoryAttribute.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu03CategoryAttribute.converter.PmsCategoryAttributeConverter;
import com.aioveu.pms.aioveu03CategoryAttribute.mapper.PmsCategoryAttributeMapper;
import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.aioveu03CategoryAttribute.model.query.PmsCategoryAttributeQuery;
import com.aioveu.pms.aioveu03CategoryAttribute.model.vo.PmsCategoryAttributeVO;
import com.aioveu.pms.aioveu03CategoryAttribute.service.PmsCategoryAttributeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PmsCategoryAttributeServiceImpl
 * @Description TODO 商品分类类型（规格，属性）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 19:50
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PmsCategoryAttributeServiceImpl extends ServiceImpl<PmsCategoryAttributeMapper, PmsCategoryAttribute> implements PmsCategoryAttributeService {


    private final PmsCategoryAttributeConverter pmsCategoryAttributeConverter;

    /**
     * 获取商品类型（规格，属性）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsCategoryAttributeVO>} 商品类型（规格，属性）分页列表
     */
    @Override
    public IPage<PmsCategoryAttributeVO> getPmsCategoryAttributePage(PmsCategoryAttributeQuery queryParams) {
        Page<PmsCategoryAttributeVO> pageVO = this.baseMapper.getPmsCategoryAttributePage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品类型（规格，属性）表单数据
     *
     * @param id 商品类型（规格，属性）ID
     * @return 商品类型（规格，属性）表单数据
     */
    @Override
    public PmsCategoryAttributeForm getPmsCategoryAttributeFormData(Long id) {
        PmsCategoryAttribute entity = this.getById(id);
        return pmsCategoryAttributeConverter.toForm(entity);
    }

    /**
     * 新增商品类型（规格，属性）
     *
     * @param formData 商品类型（规格，属性）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsCategoryAttribute(PmsCategoryAttributeForm formData) {
        PmsCategoryAttribute entity = pmsCategoryAttributeConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品类型（规格，属性）
     *
     * @param id   商品类型（规格，属性）ID
     * @param formData 商品类型（规格，属性）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsCategoryAttribute(Long id,PmsCategoryAttributeForm formData) {
        PmsCategoryAttribute entity = pmsCategoryAttributeConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品类型（规格，属性）
     *
     * @param ids 商品类型（规格，属性）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsCategoryAttributes(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品类型（规格，属性）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
