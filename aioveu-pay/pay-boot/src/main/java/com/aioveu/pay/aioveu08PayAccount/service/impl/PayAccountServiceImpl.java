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

    /**
     * 余额支付
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> balancePay(BalancePayDTO dto) {
        // 1. 查询账户
        PayAccount account = accountMapper.selectByUserIdAndType(
                dto.getUserId(),
                AccountType.USER.getCode()
        );

        if (account == null) {
            throw new BusinessException("账户不存在");
        }

        if (account.getStatus() != AccountStatus.NORMAL.getCode()) {
            throw new BusinessException("账户状态异常");
        }

        // 2. 检查余额
        BigDecimal availableBalance = account.getBalance().subtract(account.getFrozenBalance());
        if (availableBalance.compareTo(dto.getAmount()) < 0) {
            throw new BusinessException("余额不足");
        }

        // 3. 扣减余额（使用乐观锁）
        int rows = accountMapper.deductBalance(
                account.getAccountNo(),
                dto.getAmount(),
                account.getVersion()
        );

        if (rows == 0) {
            throw new ConcurrentUpdateException("账户余额并发更新失败");
        }

        // 4. 记录账户流水
        createAccountFlow(account, dto, AccountFlowType.EXPEND);

        return Result.success();
    }

    /**
     * 资金冻结
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> freezeBalance(FreezeBalanceDTO dto) {
        // 1. 查询账户
        PayAccount account = accountMapper.selectByAccountNo(dto.getAccountNo());

        // 2. 检查可用余额
        BigDecimal availableBalance = account.getAvailableBalance();
        if (availableBalance.compareTo(dto.getFreezeAmount()) < 0) {
            throw new BusinessException("可用余额不足");
        }

        // 3. 冻结资金
        int rows = accountMapper.freezeBalance(
                dto.getAccountNo(),
                dto.getFreezeAmount(),
                account.getVersion()
        );

        if (rows == 0) {
            throw new ConcurrentUpdateException("资金冻结并发更新失败");
        }

        // 4. 记录冻结流水
        AccountFlow flow = new AccountFlow();
        flow.setFlowNo(FlowNoGenerator.generateFlowNo());
        flow.setAccountNo(dto.getAccountNo());
        flow.setBizNo(dto.getBizNo());
        flow.setBizType(dto.getBizType());
        flow.setFlowType(AccountFlowType.FREEZE.getCode());
        flow.setAmount(dto.getFreezeAmount());
        flow.setBalanceBefore(account.getBalance());
        flow.setBalanceAfter(account.getBalance()); // 余额不变
        flow.setFrozenBefore(account.getFrozenBalance());
        flow.setFrozenAfter(account.getFrozenBalance().add(dto.getFreezeAmount()));
        flow.setRemark(dto.getRemark());

        flowMapper.insert(flow);

        return Result.success();
    }

}
