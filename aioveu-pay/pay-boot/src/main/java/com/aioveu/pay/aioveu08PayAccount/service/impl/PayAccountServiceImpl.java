package com.aioveu.pay.aioveu08PayAccount.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu08PayAccount.converter.PayAccountConverter;
import com.aioveu.pay.aioveu08PayAccount.mapper.PayAccountMapper;
import com.aioveu.pay.aioveu08PayAccount.model.entity.PayAccount;
import com.aioveu.pay.aioveu08PayAccount.model.form.PayAccountForm;
import com.aioveu.pay.aioveu08PayAccount.model.query.PayAccountQuery;
import com.aioveu.pay.aioveu08PayAccount.model.vo.PayAccountVO;
import com.aioveu.pay.aioveu08PayAccount.service.PayAccountService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayAccountServiceImpl
 * @Description TODO 支付账户服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:13
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayAccountServiceImpl extends ServiceImpl<PayAccountMapper, PayAccount> implements PayAccountService {

    private final PayAccountConverter payAccountConverter;

    /**
     * 获取支付账户分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayAccountVO>} 支付账户分页列表
     */
    @Override
    public IPage<PayAccountVO> getPayAccountPage(PayAccountQuery queryParams) {
        Page<PayAccountVO> pageVO = this.baseMapper.getPayAccountPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付账户表单数据
     *
     * @param id 支付账户ID
     * @return 支付账户表单数据
     */
    @Override
    public PayAccountForm getPayAccountFormData(Long id) {
        PayAccount entity = this.getById(id);
        return payAccountConverter.toForm(entity);
    }

    /**
     * 新增支付账户
     *
     * @param formData 支付账户表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayAccount(PayAccountForm formData) {
        PayAccount entity = payAccountConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付账户
     *
     * @param id   支付账户ID
     * @param formData 支付账户表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayAccount(Long id,PayAccountForm formData) {
        PayAccount entity = payAccountConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付账户
     *
     * @param ids 支付账户ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayAccounts(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付账户数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
