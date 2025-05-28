package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ProductOrder;
import com.aioveu.form.ProductOrderForm;
import com.aioveu.vo.ProductOrderManagerVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductOrderService extends IService<ProductOrder> {

    /**
     * 获取签单列表
     * @param page
     * @param size
     * @param id
     * @param name
     * @param categoryId
     * @param status
     * @return
     */
    IPage<ProductOrderManagerVO> getManagerAll(int page, int size, String id, String name, Long categoryId, Integer status);

    /**
     * 获取详情
     * @param id
     * @return
     */
    ProductOrderManagerVO managerDetail(String id);

    /**
     * 创建签单
     * @param form
     * @param username
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    ProductOrderManagerVO createProductOrder(ProductOrderForm form, String username);

    /**
     * 更新支付状态
     * @param orderPayId
     * @param payFinishTime
     * @return
     */
    boolean updateOrder2Success(String orderPayId, Date payFinishTime);

    /**
     * 获取支付二维码
     * @param id
     * @return
     */
    Map<String, String> getPayCode(String id);

    /**
     * 获取合同地址
     * @param id
     * @return
     */
    Map<String, Object> getAgreementUrl(String id);

    /**
     * 协议签署
     * @param id
     * @param image
     * @return
     */
    String agreementSign(String id, String image);

    /**
     * 上传公章
     * @param image
     * @param id
     * @return
     */
    Map<String, String> uploadChapter(MultipartFile image, String id);

    /**
     * 我的签单
     * @return
     */
    List<ProductOrderManagerVO> getMyOrder();

    /**
     * 签署
     * @param id
     * @return
     */
    Map<String, String> sign(String id);

    /**
     * 生成电子公章
     * @param name
     * @param id
     * @return
     */
    String createSeal(String name, String id);

    /**
     * 获取合同状态
     * @param id
     * @return
     */
    Map<String, Object> getAgreementStatus(String id);

}
