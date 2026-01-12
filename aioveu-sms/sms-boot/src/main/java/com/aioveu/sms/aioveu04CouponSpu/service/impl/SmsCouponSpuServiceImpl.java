package com.aioveu.sms.aioveu04CouponSpu.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.sms.aioveu04CouponSpu.converter.SmsCouponSpuConverter;
import com.aioveu.sms.aioveu04CouponSpu.model.form.SmsCouponSpuForm;
import com.aioveu.sms.aioveu04CouponSpu.model.query.SmsCouponSpuQuery;
import com.aioveu.sms.aioveu04CouponSpu.model.vo.SmsCouponSpuVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.sms.aioveu04CouponSpu.mapper.SmsCouponSpuMapper;
import com.aioveu.sms.aioveu04CouponSpu.model.entity.SmsCouponSpu;
import com.aioveu.sms.aioveu04CouponSpu.service.SmsCouponSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  优惠券适用的具体商品服务实现类
 * @Date  2026/1/12 12:18
 * @Param
 * @return
 **/
@Service
@RequiredArgsConstructor
public class SmsCouponSpuServiceImpl extends ServiceImpl<SmsCouponSpuMapper, SmsCouponSpu>
implements SmsCouponSpuService{

    private final SmsCouponSpuConverter smsCouponSpuConverter;

    /**
     * 获取优惠券适用的具体商品分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<SmsCouponSpuVO>} 优惠券适用的具体商品分页列表
     */
    @Override
    public IPage<SmsCouponSpuVO> getSmsCouponSpuPage(SmsCouponSpuQuery queryParams) {
        Page<SmsCouponSpuVO> pageVO = this.baseMapper.getSmsCouponSpuPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取优惠券适用的具体商品表单数据
     *
     * @param id 优惠券适用的具体商品ID
     * @return 优惠券适用的具体商品表单数据
     */
    @Override
    public SmsCouponSpuForm getSmsCouponSpuFormData(Long id) {
        SmsCouponSpu entity = this.getById(id);
        return smsCouponSpuConverter.toForm(entity);
    }

    /**
     * 新增优惠券适用的具体商品
     *
     * @param formData 优惠券适用的具体商品表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveSmsCouponSpu(SmsCouponSpuForm formData) {
        SmsCouponSpu entity = smsCouponSpuConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新优惠券适用的具体商品
     *
     * @param id   优惠券适用的具体商品ID
     * @param formData 优惠券适用的具体商品表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSmsCouponSpu(Long id,SmsCouponSpuForm formData) {
        SmsCouponSpu entity = smsCouponSpuConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除优惠券适用的具体商品
     *
     * @param ids 优惠券适用的具体商品ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSmsCouponSpus(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的优惠券适用的具体商品数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}




