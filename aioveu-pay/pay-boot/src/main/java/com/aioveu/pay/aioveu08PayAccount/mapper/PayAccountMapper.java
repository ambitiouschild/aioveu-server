package com.aioveu.pay.aioveu08PayAccount.mapper;

import com.aioveu.pay.aioveu08PayAccount.model.entity.PayAccount;
import com.aioveu.pay.aioveu08PayAccount.model.query.PayAccountQuery;
import com.aioveu.pay.aioveu08PayAccount.model.vo.PayAccountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayAccountMapper
 * @Description TODO 支付账户Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:10
 * @Version 1.0
 **/
@Mapper
public interface PayAccountMapper extends BaseMapper<PayAccount> {

    /**
     * 获取支付账户分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayAccountVO>} 支付账户分页列表
     */
    Page<PayAccountVO> getPayAccountPage(Page<PayAccountVO> page, PayAccountQuery queryParams);
}
