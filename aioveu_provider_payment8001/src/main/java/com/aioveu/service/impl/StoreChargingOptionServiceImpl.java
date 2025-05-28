package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreChargingOptionDao;
import com.aioveu.entity.*;
import com.aioveu.enums.ChargingOptionEnum;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.service.*;
import com.aioveu.vo.StoreChargingOptionDetailVO;
import com.aioveu.vo.StoreChargingOptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class StoreChargingOptionServiceImpl extends ServiceImpl<StoreChargingOptionDao, StoreChargingOption> implements StoreChargingOptionService {

    @Autowired
    private ChargingOptionService  chargingOptionService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private MQMessageService mqMessageService;

    @Override
    public List<StoreChargingOptionVO> getStoreChargingOption(Long storeId) {
        QueryWrapper<StoreChargingOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreChargingOption::getStoreId, storeId)
                .eq(StoreChargingOption::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(StoreChargingOption::getTotal);

        List<StoreChargingOption> list = list(queryWrapper);
        return list.stream().map(item -> {
            StoreChargingOptionVO vo = new StoreChargingOptionVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setTotal(item.getTotal());
            vo.setCode(item.getChargingCode());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public StoreChargingOption getByStoreIdAndCode(Long storeId, String code) {
        QueryWrapper<StoreChargingOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreChargingOption::getStoreId, storeId)
                .eq(StoreChargingOption::getStatus, DataStatus.NORMAL.getCode())
                .eq(StoreChargingOption::getChargingCode, code);
        return getOne(queryWrapper);
    }

    @Override
    public StoreChargingOption getStoreChargingByCompanyId(Long companyId, String code) {
        QueryWrapper<StoreChargingOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreChargingOption::getCompanyId, companyId)
                .eq(StoreChargingOption::getStatus, DataStatus.NORMAL.getCode())
                .eq(StoreChargingOption::getChargingCode, code)
                .orderByDesc(StoreChargingOption::getTotal).last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public StoreChargingOptionDetailVO getStoreChargingOptionDetail(String chargingCode, Long storeId) {
        StoreChargingOption storeChargingOption = getByStoreIdAndCode(storeId, chargingCode);
        if (storeChargingOption != null) {
            StoreChargingOptionDetailVO vo = new StoreChargingOptionDetailVO();
            vo.setTotal(storeChargingOption.getTotal());

            ChargingOption chargingOption = chargingOptionService.getByCode(chargingCode);
            vo.setMinAmount(chargingOption.getMinAmount());
            vo.setPrice(chargingOption.getPrice());
            vo.setChargingOptionId(chargingOption.getId());
            vo.setStoreChargingOptionId(storeChargingOption.getId());
            return vo;
        }
        return null;
    }

    @Override
    public void charging(Long companyId, Long storeId, ChargingOptionEnum chargingOption, int count) {
        Company company = companyService.getById(companyId);
        // 按量计费 开始扣费
        if (company.getCompanyType() == 1) {
            ChargingChange chargingChange = new ChargingChange();
            chargingChange.setChargingCode(chargingOption.getCode());
            chargingChange.setStoreId(storeId);
            chargingChange.setCompanyId(companyId);
            chargingChange.setCount(count);
            chargingChange.setChangeType(0);
            chargingChange.setName(chargingOption.getDescription() + "扣费");
            chargingChange.setDescription("消息发送扣费，扣费次数:" + count);
            mqMessageService.sendChargingChange(chargingChange);
        }
    }

    @Override
    public boolean chargingCheck(Long companyId, Long storeId, ChargingOptionEnum chargingOption, int count) {
        if (companyId == null) {
            return true;
        }
        Company company = companyService.getById(companyId);
        // 包年 直接返回
        if (company.getCompanyType() == 0) {
            return true;
        }
        StoreChargingOption storeChargingOption;
        if (storeId == null) {
            storeChargingOption = getStoreChargingByCompanyId(companyId, chargingOption.getCode());
        } else {
            storeChargingOption = getByStoreIdAndCode(storeId, chargingOption.getCode());
        }
        if (storeChargingOption == null) {
            log.error("店铺:{}未配置增值服务:{}", storeId, chargingOption.getDescription());
            noValueCharging(companyId, storeId, chargingOption, "未配置" + chargingOption.getDescription() + "服务");
            return false;
        }
        if (storeChargingOption.getTotal() < count) {
            log.error("店铺:{}增值服务:{},需要:{} 剩下:{}不足扣费", storeChargingOption.getStoreId(), chargingOption.getDescription(), count, storeChargingOption.getTotal());
            noValueCharging(storeChargingOption.getCompanyId(), storeChargingOption.getStoreId(), chargingOption, "次数已用完，服务暂不可用，请尽快充值");
            return false;
        }
        return true;
    }

    @Override
    public void noValueCharging(Long companyId, Long storeId, ChargingOptionEnum chargingOption, String tips) {
        Store store = storeService.getById(storeId);
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("storeName", store.getName());
        msgMap.put("chargingName", chargingOption.getDescription());
        msgMap.put("tips", tips);
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.CHARGING_NO_VALUE.getCode(), storeId);
    }
}
