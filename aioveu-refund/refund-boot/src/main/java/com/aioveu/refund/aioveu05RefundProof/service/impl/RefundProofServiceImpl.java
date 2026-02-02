package com.aioveu.refund.aioveu05RefundProof.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu05RefundProof.converter.RefundProofConverter;
import com.aioveu.refund.aioveu05RefundProof.mapper.RefundProofMapper;
import com.aioveu.refund.aioveu05RefundProof.model.entity.RefundProof;
import com.aioveu.refund.aioveu05RefundProof.model.form.RefundProofForm;
import com.aioveu.refund.aioveu05RefundProof.model.query.RefundProofQuery;
import com.aioveu.refund.aioveu05RefundProof.model.vo.RefundProofVO;
import com.aioveu.refund.aioveu05RefundProof.service.RefundProofService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: RefundProofServiceImpl
 * @Description TODO 退款凭证图片服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:07
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundProofServiceImpl extends ServiceImpl<RefundProofMapper, RefundProof> implements RefundProofService {

    private final RefundProofConverter refundProofConverter;

    /**
     * 获取退款凭证图片分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundProofVO>} 退款凭证图片分页列表
     */
    @Override
    public IPage<RefundProofVO> getRefundProofPage(RefundProofQuery queryParams) {
        Page<RefundProofVO> pageVO = this.baseMapper.getRefundProofPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款凭证图片表单数据
     *
     * @param id 退款凭证图片ID
     * @return 退款凭证图片表单数据
     */
    @Override
    public RefundProofForm getRefundProofFormData(Long id) {
        RefundProof entity = this.getById(id);
        return refundProofConverter.toForm(entity);
    }

    /**
     * 获取退款凭证图片实体List
     *
     * @param refundId 退款ID
     * @return 退款凭证图片实体List
     */
    @Override
    public List<RefundProof> getRefundProofEntityByRefundId(Long refundId) {
        List<RefundProof> items  = this.list(new LambdaQueryWrapper<RefundProof>()
                .eq(RefundProof::getRefundId, refundId)
        );

        log.info("获取退款凭证图片实体List:{}",items);

        return items;
    }


    /**
     * 新增退款凭证图片
     *
     * @param formData 退款凭证图片表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundProof(RefundProofForm formData) {
        RefundProof entity = refundProofConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 新增退款凭证图片
     *
     * @param proofImages 退款凭证图片表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundProofs(List<RefundProofForm> proofImages, Long refundId) {

        List<RefundProof> proofs = proofImages.stream().map(url -> {
            RefundProof proof = new RefundProof();
            proof.setRefundId(refundId);
            proof.setProofType(1); // 1-图片
//            proof.setImageUrl(url);
            return proof;
        }).collect(Collectors.toList());

        RefundProof proof = new RefundProof();

        return this.save(proof);
    }

    /**
     * 更新退款凭证图片
     *
     * @param id   退款凭证图片ID
     * @param formData 退款凭证图片表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundProof(Long id,RefundProofForm formData) {
        RefundProof entity = refundProofConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款凭证图片
     *
     * @param ids 退款凭证图片ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundProofs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款凭证图片数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
