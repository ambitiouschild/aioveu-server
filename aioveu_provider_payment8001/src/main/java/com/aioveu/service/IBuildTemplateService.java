package com.aioveu.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.exception.SportException;
import com.aioveu.vo.CouponTemplateItemVO;
import com.aioveu.vo.TemplateRequest;

/**
 * <h1>构建优惠券模板接口定义</h1>
 * Created by Qinyi.
 */
public interface IBuildTemplateService extends IService<CouponTemplate> {

    /**
     * <h2>创建优惠券模板</h2>
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return {@link CouponTemplate} 优惠券模板实体
     * */
    CouponTemplate buildTemplate(TemplateRequest request)
            throws SportException;

    /**
     * 获取优惠券模板列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<CouponTemplateItemVO> managerList(int page, int size, Long storeId);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);
}
