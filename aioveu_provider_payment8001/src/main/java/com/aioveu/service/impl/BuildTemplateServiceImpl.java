package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CouponTemplateDao;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.exception.SportException;
import com.aioveu.service.CouponTemplateService;
import com.aioveu.service.IAsyncService;
import com.aioveu.service.IBuildTemplateService;
import com.aioveu.vo.CouponTemplateItemVO;
import com.aioveu.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description <h1>构建优惠券模板接口实现</h1>
 * @author: 雒世松
 * @date: 2025/1/28 0028 16:06
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl extends ServiceImpl<CouponTemplateDao, CouponTemplate> implements IBuildTemplateService {

    /** 异步服务 */
    @Autowired
    private IAsyncService asyncService;

    /** CouponTemplate Dao */
    @Autowired
    private CouponTemplateService couponTemplateService;

    /**
     * <h2>创建优惠券模板</h2>
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return {@link CouponTemplate} 优惠券模板实体
     * @throws SportException
     */
    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws SportException {
        // 参数合法性校验
        if (!request.validate()) {
            throw new SportException("参数错误!");
        }

        if (request.getCount() != null && request.getCount() > 10000) {
            throw new SportException("优惠券数量不能大于1000!");
        }

        // 构造 CouponTemplate 并保存到数据库中
        CouponTemplate template = requestToTemplate(request);
        if (request.getId() != null && request.getId() > 0) {
            template.setId(request.getId());
            couponTemplateService.saveOrUpdate(template);
        } else {
            // 判断同名的优惠券模板是否存在
            if (null != couponTemplateService.findByName(request.getName())) {
                throw new SportException("优惠券名称重复!");
            }
            template.setCreateDate(new Date());
            couponTemplateService.save(template);
            // 根据优惠券模板异步生成优惠券码
            asyncService.asyncConstructCouponByTemplate(template);
        }
        return template;
    }

    @Override
    public IPage<CouponTemplateItemVO> managerList(int page, int size, Long storeId) {
        QueryWrapper<CouponTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(CouponTemplate::getStatus, 0);
        if (storeId != null) {
            queryWrapper.lambda().eq(CouponTemplate::getStoreId, storeId);
        }
        queryWrapper.lambda().orderByAsc(CouponTemplate::getCreateDate);
        IPage<CouponTemplate> couponTemplateIPage = page(new Page<>(page, size), queryWrapper);
        List<CouponTemplateItemVO> records = couponTemplateIPage.getRecords().stream().map(item -> {
            CouponTemplateItemVO couponTemplateItemVO = new CouponTemplateItemVO();
            BeanUtils.copyProperties(item, couponTemplateItemVO);
            return couponTemplateItemVO;
        }).collect(Collectors.toList());

        IPage<CouponTemplateItemVO> iPage = new Page<>();
        BeanUtils.copyProperties(couponTemplateIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setId(id);
        couponTemplate.setStatus(status);
        return saveOrUpdate(couponTemplate);
    }

    /**
     * <h2>将 TemplateRequest 转换为 CouponTemplate</h2>
     * */
    private CouponTemplate requestToTemplate(TemplateRequest request) {
        CouponTemplate couponTemplate = new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getTarget(),
                request.getRule(),
                request.getShowStore()
        );
        couponTemplate.setActivePrice(request.getActivePrice());
        couponTemplate.setCompanyId(request.getCompanyId());
        couponTemplate.setStoreId(request.getStoreId());
        couponTemplate.setProductId(request.getProductId());
        couponTemplate.setVipPriceCanUse(request.getVipPriceCanUse());
        return couponTemplate;
    }
}
