package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RechargeOption;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RechargeOptionService extends IService<RechargeOption> {

    /**
     * 获取充值选项
     * @return
     */
    List<RechargeOption> getRechargeList();

}
