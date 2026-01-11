package com.aioveu.pms.aioveu07SpuAttribute.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu07SpuAttribute.converter.PmsSpuAttributeConverter;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import com.aioveu.pms.aioveu07SpuAttribute.model.query.PmsSpuAttributeQuery;
import com.aioveu.pms.aioveu07SpuAttribute.model.vo.PmsSpuAttributeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu07SpuAttribute.mapper.PmsSpuAttributeMapper;
import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import com.aioveu.pms.aioveu07SpuAttribute.service.PmsSpuAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品类型（属性/规格）服务实现类
 * @Date  2026/1/11 22:11
 * @Param
 * @return
 **/

@Service
@RequiredArgsConstructor
public class PmsSpuAttributeServiceImpl extends ServiceImpl<PmsSpuAttributeMapper, PmsSpuAttribute> implements PmsSpuAttributeService {


    private final PmsSpuAttributeConverter pmsSpuAttributeConverter;

    /**
     * 获取商品类型（属性/规格）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsSpuAttributeVO>} 商品类型（属性/规格）分页列表
     */
    @Override
    public IPage<PmsSpuAttributeVO> getPmsSpuAttributePage(PmsSpuAttributeQuery queryParams) {
        Page<PmsSpuAttributeVO> pageVO = this.baseMapper.getPmsSpuAttributePage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品类型（属性/规格）表单数据
     *
     * @param id 商品类型（属性/规格）ID
     * @return 商品类型（属性/规格）表单数据
     */
    @Override
    public PmsSpuAttributeForm getPmsSpuAttributeFormData(Long id) {
        PmsSpuAttribute entity = this.getById(id);
        return pmsSpuAttributeConverter.toForm(entity);
    }

    /**
     * 新增商品类型（属性/规格）
     *
     * @param formData 商品类型（属性/规格）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsSpuAttribute(PmsSpuAttributeForm formData) {
        PmsSpuAttribute entity = pmsSpuAttributeConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品类型（属性/规格）
     *
     * @param id   商品类型（属性/规格）ID
     * @param formData 商品类型（属性/规格）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsSpuAttribute(Long id,PmsSpuAttributeForm formData) {
        PmsSpuAttribute entity = pmsSpuAttributeConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品类型（属性/规格）
     *
     * @param ids 商品类型（属性/规格）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsSpuAttributes(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品类型（属性/规格）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
