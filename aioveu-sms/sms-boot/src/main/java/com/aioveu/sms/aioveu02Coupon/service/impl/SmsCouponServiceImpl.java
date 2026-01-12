package com.aioveu.sms.aioveu02Coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.sms.aioveu02Coupon.model.form.SmsCouponForm;
import com.aioveu.sms.aioveu02Coupon.model.vo.SmsCouponVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.base.IBaseEnum;
import com.aioveu.sms.aioveu02Coupon.converter.SmsCouponConverter;
import com.aioveu.sms.aioveu02Coupon.enums.CouponApplicationScopeEnum;
import com.aioveu.sms.aioveu02Coupon.mapper.SmsCouponMapper;
import com.aioveu.sms.aioveu02Coupon.model.entity.SmsCoupon;
import com.aioveu.sms.aioveu04CouponSpu.model.entity.SmsCouponSpu;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.entity.SmsCouponSpuCategory;
import com.aioveu.sms.aioveu02Coupon.model.query.SmsCouponQuery;
import com.aioveu.sms.aioveu02Coupon.service.SmsCouponService;
import com.aioveu.sms.aioveu05CouponSpuCategory.service.SmsCouponSpuCategoryService;
import com.aioveu.sms.aioveu04CouponSpu.service.SmsCouponSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO 优惠券业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:48
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
public class SmsCouponServiceImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements SmsCouponService {

    private final SmsCouponConverter smsCouponConverter;

    private final SmsCouponSpuCategoryService smsCouponSpuCategoryService;

    private final SmsCouponSpuService smsCouponSpuService;

//    /**
//     * 获取优惠券分页列表
//     *
//     * @param queryParams 查询参数
//     * @return {@link IPage<SmsCouponVO>} 优惠券分页列表
//     */
//    @Override
//    public IPage<SmsCouponVO> getSmsCouponPage(SmsCouponQuery queryParams) {
//        Page<SmsCouponVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
//        // 查询数据
//        List<SmsCoupon> couponList = this.baseMapper.getCouponPage(page, queryParams);
//        // 实体转换
//        List<SmsCouponVO> records = smsCouponConverter.entity2PageVO(couponList);
//        page.setRecords(records);
//        return page;
//    }

    /**
     * 获取优惠券分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsCouponVO>} 优惠券分页列表
     */
    @Override
    public IPage<SmsCouponVO> getSmsCouponPage(SmsCouponQuery queryParams) {
        Page<SmsCouponVO> pageVO = this.baseMapper.getSmsCouponPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取优惠券表单数据
     *
     * @param id 优惠券ID
     * @return 优惠券表单数据
     */
    @Override
    public SmsCouponForm getSmsCouponFormData(Long id) {
        SmsCoupon entity = this.getById(id);
        // 实体转换entity->form
        SmsCouponForm smsCouponForm = smsCouponConverter.entity2Form(entity);

        Integer applicationScope = smsCouponForm.getApplicationScope();

        CouponApplicationScopeEnum applicationScopeEnum = IBaseEnum.getEnumByValue(applicationScope, CouponApplicationScopeEnum.class);
        if (applicationScopeEnum != null) {
            switch (applicationScopeEnum) {
                case SPU_CATEGORY:
                    List<SmsCouponSpuCategory> couponSpuCategoryList = smsCouponSpuCategoryService.list(new LambdaQueryWrapper<SmsCouponSpuCategory>()
                            .eq(SmsCouponSpuCategory::getCouponId, id)
                            .select(SmsCouponSpuCategory::getCategoryId)
                    );
                    List<Long> categoryIds = couponSpuCategoryList.stream().map(item -> item.getCategoryId()).collect(Collectors.toList());
                    smsCouponForm.setSpuCategoryIds(categoryIds);
                    break;
                case SPU:
                    List<SmsCouponSpu> couponSpuList = smsCouponSpuService.list(new LambdaQueryWrapper<SmsCouponSpu>()
                            .eq(SmsCouponSpu::getCouponId, id)
                            .select(SmsCouponSpu::getSpuId)
                    );
                    List<Long> spuIds = couponSpuList.stream().map(item -> item.getSpuId()).collect(Collectors.toList());
                    smsCouponForm.setSpuIds(spuIds);
                    break;
            }
        }
        return smsCouponForm;
    }

    /**
     * 新增优惠券
     *
     * @param formData 优惠券表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsCoupon(SmsCouponForm formData) {
        SmsCoupon entity = smsCouponConverter.form2Entity(formData);
        boolean result = this.save(entity);

        if (result) {
            // 根据优惠券适用商品范围保存对应的关联关系
            // 指定商品分类： 优惠券 <-> 商品分类
            // 指定商品： 优惠券 <-> 商品
            Long couponId = entity.getId();
            Integer applicationScope = formData.getApplicationScope();
            CouponApplicationScopeEnum applicationScopeEnum = IBaseEnum.getEnumByValue(applicationScope, CouponApplicationScopeEnum.class);

            Assert.isTrue(applicationScopeEnum != null, "请指定优惠券适用范围");
            switch (applicationScopeEnum) {
                case SPU_CATEGORY:
                    List<Long> spuCategoryIds = formData.getSpuCategoryIds();
                    if (CollectionUtil.isNotEmpty(spuCategoryIds)) {
                        List<SmsCouponSpuCategory> smsCouponSpuCategories = spuCategoryIds.stream()
                                .map(spuCategoryId -> new SmsCouponSpuCategory().setCouponId(couponId).setCategoryId(spuCategoryId))
                                .collect(Collectors.toList());
                        smsCouponSpuCategoryService.saveBatch(smsCouponSpuCategories);
                    }
                    break;

                case SPU:
                    List<Long> spuIds = formData.getSpuIds();
                    if (CollectionUtil.isNotEmpty(spuIds)) {
                        List<SmsCouponSpu> smsCouponSpuList = spuIds.stream()
                                .map(spuId -> new SmsCouponSpu().setCouponId(couponId).setSpuId(spuId))
                                .collect(Collectors.toList());
                        smsCouponSpuService.saveBatch(smsCouponSpuList);
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * 更新优惠券
     *
     * @param id   优惠券ID
     * @param formData 优惠券表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsCoupon(Long id, SmsCouponForm formData) {
        SmsCoupon entity = smsCouponConverter.form2Entity(formData);
        boolean result = this.updateById(entity);

        if (result) {
            // 根据优惠券适用商品范围保存对应的关联关系
            // 全场通用： 删除所有关联
            // 指定商品分类： 优惠券 <-> 商品分类
            // 指定商品： 优惠券 <-> 商品
            Integer applicationScope = formData.getApplicationScope();
            CouponApplicationScopeEnum applicationScopeEnum = IBaseEnum.getEnumByValue(applicationScope, CouponApplicationScopeEnum.class);

            Assert.isTrue(applicationScopeEnum != null, "请指定优惠券适用范围");
            switch (applicationScopeEnum) {
                case ALL:
                    smsCouponSpuCategoryService.remove(new LambdaQueryWrapper<SmsCouponSpuCategory>()
                            .eq(SmsCouponSpuCategory::getCouponId, id)
                    );
                    smsCouponSpuService.remove(new LambdaQueryWrapper<SmsCouponSpu>()
                            .eq(SmsCouponSpu::getCouponId, id)
                    );

                    break;
                case SPU_CATEGORY:
                    List<Long> spuCategoryIds = formData.getSpuCategoryIds();
                    if (CollectionUtil.isNotEmpty(spuCategoryIds)) {
                        smsCouponSpuCategoryService.remove(new LambdaQueryWrapper<SmsCouponSpuCategory>()
                                .eq(SmsCouponSpuCategory::getCouponId, id)
                        );
                        List<SmsCouponSpuCategory> smsCouponSpuCategories = spuCategoryIds.stream()
                                .map(spuCategoryId -> new SmsCouponSpuCategory().setCouponId(id)
                                        .setCategoryId(spuCategoryId))
                                .collect(Collectors.toList());
                        smsCouponSpuCategoryService.saveBatch(smsCouponSpuCategories);
                    }
                    break;
                case SPU:
                    List<Long> spuIds = formData.getSpuIds();
                    if (CollectionUtil.isNotEmpty(spuIds)) {
                        smsCouponSpuService.remove(new LambdaQueryWrapper<SmsCouponSpu>()
                                .eq(SmsCouponSpu::getCouponId, id)
                        );
                        List<SmsCouponSpu> smsCouponSpuList = spuIds.stream()
                                .map(spuId -> new SmsCouponSpu().setCouponId(id).setSpuId(spuId))
                                .collect(Collectors.toList());
                        smsCouponSpuService.saveBatch(smsCouponSpuList);
                    }
                    break;
            }
        }

        return result;
    }


    /**
     * 删除优惠券
     *
     * @param ids 优惠券ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsCoupons(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的优惠券数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.asList(ids.split(",")).stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        boolean result = this.removeByIds(idList);
        return result;
    }


}




