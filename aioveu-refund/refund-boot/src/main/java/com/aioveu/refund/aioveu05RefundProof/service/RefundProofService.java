package com.aioveu.refund.aioveu05RefundProof.service;

import com.aioveu.refund.aioveu05RefundProof.model.entity.RefundProof;
import com.aioveu.refund.aioveu05RefundProof.model.form.RefundProofForm;
import com.aioveu.refund.aioveu05RefundProof.model.query.RefundProofQuery;
import com.aioveu.refund.aioveu05RefundProof.model.vo.RefundProofVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: RefundProofService
 * @Description TODO  退款凭证图片服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:06
 * @Version 1.0
 **/
public interface RefundProofService extends IService<RefundProof> {

    /**
     *退款凭证图片分页列表
     *
     * @return {@link IPage<RefundProofVO>} 退款凭证图片分页列表
     */
    IPage<RefundProofVO> getRefundProofPage(RefundProofQuery queryParams);

    /**
     * 获取退款凭证图片表单数据
     *
     * @param id 退款凭证图片ID
     * @return 退款凭证图片表单数据
     */
    RefundProofForm getRefundProofFormData(Long id);

    /**
     * 获取退款凭证图片实体List
     *
     * @param refundId 退款ID
     * @return 退款凭证图片实体List
     */
    List<RefundProof> getRefundProofEntityByRefundId(Long refundId);

    /**
     * 新增退款凭证图片
     *
     * @param formData 退款凭证图片表单对象
     * @return 是否新增成功
     */
    boolean saveRefundProof(RefundProofForm formData);


    /**
     * 新增退款凭证图片
     *
     * @param proofImages 退款凭证图片表单对象
     * @return 是否新增成功
     */
    boolean saveRefundProofs(List<RefundProofForm> proofImages, Long refundId);

    /**
     * 修改退款凭证图片
     *
     * @param id   退款凭证图片ID
     * @param formData 退款凭证图片表单对象
     * @return 是否修改成功
     */
    boolean updateRefundProof(Long id, RefundProofForm formData);

    /**
     * 删除退款凭证图片
     *
     * @param ids 退款凭证图片ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundProofs(String ids);
}
