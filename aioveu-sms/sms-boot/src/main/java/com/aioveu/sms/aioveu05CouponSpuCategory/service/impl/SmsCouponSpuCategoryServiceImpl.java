package com.aioveu.sms.aioveu05CouponSpuCategory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.sms.aioveu05CouponSpuCategory.converter.SmsCouponSpuCategoryConverter;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.form.SmsCouponSpuCategoryForm;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.query.SmsCouponSpuCategoryQuery;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.vo.SmsCouponSpuCategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.sms.aioveu05CouponSpuCategory.mapper.SmsCouponSpuCategoryMapper;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.entity.SmsCouponSpuCategory;
import com.aioveu.sms.aioveu05CouponSpuCategory.service.SmsCouponSpuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券适用的具体分类服务实现类
 * @Date  2026/1/12 13:11
 * @Param
 * @return
 **/

@Service
@RequiredArgsConstructor
public class SmsCouponSpuCategoryServiceImpl extends ServiceImpl<SmsCouponSpuCategoryMapper, SmsCouponSpuCategory>
implements SmsCouponSpuCategoryService{

    private final SmsCouponSpuCategoryConverter smsCouponSpuCategoryConverter;

    /**
     * 获取优惠券适用的具体分类分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsCouponSpuCategoryVO>} 优惠券适用的具体分类分页列表
     */
    @Override
    public IPage<SmsCouponSpuCategoryVO> getSmsCouponSpuCategoryPage(SmsCouponSpuCategoryQuery queryParams) {
        Page<SmsCouponSpuCategoryVO> pageVO = this.baseMapper.getSmsCouponSpuCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取优惠券适用的具体分类表单数据
     *
     * @param id 优惠券适用的具体分类ID
     * @return 优惠券适用的具体分类表单数据
     */
    @Override
    public SmsCouponSpuCategoryForm getSmsCouponSpuCategoryFormData(Long id) {
        SmsCouponSpuCategory entity = this.getById(id);
        return smsCouponSpuCategoryConverter.toForm(entity);
    }

    /**
     * 新增优惠券适用的具体分类
     *
     * @param formData 优惠券适用的具体分类表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsCouponSpuCategory(SmsCouponSpuCategoryForm formData) {
        SmsCouponSpuCategory entity = smsCouponSpuCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新优惠券适用的具体分类
     *
     * @param id   优惠券适用的具体分类ID
     * @param formData 优惠券适用的具体分类表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsCouponSpuCategory(Long id,SmsCouponSpuCategoryForm formData) {
        SmsCouponSpuCategory entity = smsCouponSpuCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除优惠券适用的具体分类
     *
     * @param ids 优惠券适用的具体分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsCouponSpuCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的优惠券适用的具体分类数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}




