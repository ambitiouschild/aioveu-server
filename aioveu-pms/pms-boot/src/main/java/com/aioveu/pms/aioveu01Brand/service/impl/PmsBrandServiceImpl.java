package com.aioveu.pms.aioveu01Brand.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pms.aioveu01Brand.converter.PmsBrandConverter;
import com.aioveu.pms.aioveu01Brand.model.form.PmsBrandForm;
import com.aioveu.pms.aioveu01Brand.model.query.PmsBrandQuery;
import com.aioveu.pms.aioveu01Brand.model.vo.PmsBrandVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu01Brand.mapper.PmsBrandMapper;
import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;
import com.aioveu.pms.aioveu01Brand.service.PmsBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品品牌服务实现类
 * @Date  2026/1/10 19:06
 * @Param
 * @return
 **/


@Service
@RequiredArgsConstructor
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandService {

    private final PmsBrandConverter pmsBrandConverter;

    /**
     * 获取商品品牌分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsBrandVO>} 商品品牌分页列表
     */
    @Override
    public IPage<PmsBrandVO> getPmsBrandPage(PmsBrandQuery queryParams) {
        Page<PmsBrandVO> pageVO = this.baseMapper.getPmsBrandPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品品牌表单数据
     *
     * @param id 商品品牌ID
     * @return 商品品牌表单数据
     */
    @Override
    public PmsBrandForm getPmsBrandFormData(Long id) {
        PmsBrand entity = this.getById(id);
        return pmsBrandConverter.toForm(entity);
    }

    /**
     * 新增商品品牌
     *
     * @param formData 商品品牌表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsBrand(PmsBrandForm formData) {
        PmsBrand entity = pmsBrandConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品品牌
     *
     * @param id   商品品牌ID
     * @param formData 商品品牌表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsBrand(Long id,PmsBrandForm formData) {
        PmsBrand entity = pmsBrandConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品品牌
     *
     * @param ids 商品品牌ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsBrands(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品品牌数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
