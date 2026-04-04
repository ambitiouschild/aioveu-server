package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.converter.ManagerMenuHomeBannerConverter;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.mapper.ManagerMenuHomeBannerMapper;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity.ManagerMenuHomeBanner;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.form.ManagerMenuHomeBannerForm;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.query.ManagerMenuHomeBannerQuery;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.vo.ManagerMenuHomeBannerVo;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.service.ManagerMenuHomeBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ManagerMenuHomeBannerServiceImpl
 * @Description TODO 管理端app首页滚播栏服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:43
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ManagerMenuHomeBannerServiceImpl extends ServiceImpl<ManagerMenuHomeBannerMapper, ManagerMenuHomeBanner> implements ManagerMenuHomeBannerService {


    private final ManagerMenuHomeBannerConverter managerMenuHomeBannerConverter;

    /**
     * 获取管理端app首页滚播栏分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<ManagerMenuHomeBannerVo>} 管理端app首页滚播栏分页列表
     */
    @Override
    public IPage<ManagerMenuHomeBannerVo> getManagerMenuHomeBannerPage(ManagerMenuHomeBannerQuery queryParams) {
        Page<ManagerMenuHomeBannerVo> page = this.baseMapper.getManagerMenuHomeBannerPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理端app首页滚播栏表单数据
     *
     * @param id 管理端app首页滚播栏ID
     * @return 管理端app首页滚播栏表单数据
     */
    @Override
    public ManagerMenuHomeBannerForm getManagerMenuHomeBannerFormData(Long id) {
        ManagerMenuHomeBanner entity = this.getById(id);
        return managerMenuHomeBannerConverter.toForm(entity);
    }

    /**
     * 新增管理端app首页滚播栏
     *
     * @param formData 管理端app首页滚播栏表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveManagerMenuHomeBanner(ManagerMenuHomeBannerForm formData) {
        ManagerMenuHomeBanner entity = managerMenuHomeBannerConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理端app首页滚播栏
     *
     * @param id   管理端app首页滚播栏ID
     * @param formData 管理端app首页滚播栏表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateManagerMenuHomeBanner(Long id,ManagerMenuHomeBannerForm formData) {
        ManagerMenuHomeBanner entity = managerMenuHomeBannerConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理端app首页滚播栏
     *
     * @param ids 管理端app首页滚播栏ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteManagerMenuHomeBanners(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理端app首页滚播栏数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



}
