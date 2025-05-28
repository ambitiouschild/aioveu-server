package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ChargingOptionDao;
import com.aioveu.entity.ChargingChange;
import com.aioveu.entity.ChargingOption;
import com.aioveu.entity.StoreChargingOption;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.ChargingChangeService;
import com.aioveu.service.ChargingOptionService;
import com.aioveu.service.StoreChargingOptionService;
import com.aioveu.vo.IdNameCodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ChargingOptionServiceImpl extends ServiceImpl<ChargingOptionDao, ChargingOption> implements ChargingOptionService {

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Autowired
    private ChargingChangeService chargingChangeService;

    private List<ChargingOption> getChargingOptions() {
        QueryWrapper<ChargingOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChargingOption::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public boolean initStoreChargingOption(Long storeId, Long companyId) {
        List<ChargingOption> list = getChargingOptions();
        List<StoreChargingOption> storeChargingOptionList = list.stream().map(item -> {
            StoreChargingOption storeChargingOption = new StoreChargingOption();
            storeChargingOption.setName(item.getName());
            storeChargingOption.setChargingCode(item.getChargingCode());
            storeChargingOption.setStoreId(storeId);
            storeChargingOption.setCompanyId(companyId);
            storeChargingOption.setTotal(item.getGiftCount());
            return storeChargingOption;
        }).collect(Collectors.toList());

        // 增加变动记录
        List<ChargingChange> chargingChangeList = list.stream().filter(item -> item.getGiftCount() != null && item.getGiftCount() > 0)
                .map(item -> {
                    ChargingChange chargingChange = new ChargingChange();
                    chargingChange.setCompanyId(companyId);
                    chargingChange.setStoreId(storeId);
                    chargingChange.setName(item.getName());
                    chargingChange.setChargingCode(item.getChargingCode());
                    chargingChange.setChangeType(1);
                    chargingChange.setCount(item.getGiftCount());
                    chargingChange.setRemainCount(item.getGiftCount());
                    chargingChange.setDescription(item.getName() + "系统赠送");
                    return chargingChange;
                }).collect(Collectors.toList());

        chargingChangeService.saveBatch(chargingChangeList);
        return storeChargingOptionService.saveBatch(storeChargingOptionList);
    }

    @Override
    public List<IdNameCodeVO> getChargingOptionList() {
        List<ChargingOption> list = getChargingOptions();
        return list.stream().map(item -> {
            IdNameCodeVO idNameCodeVO = new IdNameCodeVO();
            idNameCodeVO.setId(item.getId());
            idNameCodeVO.setName(item.getName());
            idNameCodeVO.setCode(item.getChargingCode());
            return idNameCodeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public ChargingOption getByCode(String code) {
        QueryWrapper<ChargingOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChargingOption::getStatus, DataStatus.NORMAL.getCode())
                .eq(ChargingOption::getChargingCode, code);
        return getOne(queryWrapper);
    }
}
