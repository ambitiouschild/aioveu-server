package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CheckRecordAccountConfigDao;
import com.aioveu.entity.CheckRecordAccountConfig;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.CheckRecordAccountConfigService;
import com.aioveu.utils.EncryptionUtil;
import com.aioveu.vo.CheckRecordAccountConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/11/27 10:46
 * @Describe：
 */
@Slf4j
@Service
public class CheckRecordAccountConfigServiceImpl extends ServiceImpl<CheckRecordAccountConfigDao, CheckRecordAccountConfig> implements CheckRecordAccountConfigService {

    @Override
    public IPage<CheckRecordAccountConfigVO> getPageList(Integer page, Integer size, Long storeId) {
        QueryWrapper<CheckRecordAccountConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(CheckRecordAccountConfig::getStatus, DataStatus.DELETE.getCode())
                .eq(CheckRecordAccountConfig::getStoreId, storeId)
                .orderByAsc(CheckRecordAccountConfig::getCreateDate)
                .orderByAsc(CheckRecordAccountConfig::getStatus);
        IPage<CheckRecordAccountConfig> pageList = page(new Page<>(page, size), queryWrapper);
        List<CheckRecordAccountConfig> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)){
            return null;
        }
        List<CheckRecordAccountConfigVO> dtoList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            CheckRecordAccountConfigVO configVO = new CheckRecordAccountConfigVO();
            BeanUtils.copyProperties(records.get(i),configVO);
            dtoList.add(configVO);
        }
        IPage<CheckRecordAccountConfigVO> configVOPage = new Page<>();
        configVOPage.setRecords(dtoList);
        configVOPage.setTotal(pageList.getTotal());
        return configVOPage;
    }

    @Override
    public CheckRecordAccountConfigVO detail(String id) {
        CheckRecordAccountConfig config = getById(id);
        CheckRecordAccountConfigVO vo = new CheckRecordAccountConfigVO();
        if (config != null){
            BeanUtils.copyProperties(config, vo);
        }
        return vo;
    }

    @Override
    public List<CheckRecordAccountConfig> getListByStoreId(Long storeId) {
        QueryWrapper<CheckRecordAccountConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CheckRecordAccountConfig::getStoreId, storeId)
                .eq(CheckRecordAccountConfig::getStatus, 1);
        return list(wrapper);
    }


    @Override
    public CheckRecordAccountConfig getOneByStoreId(Long storeId, Long code) {
        QueryWrapper<CheckRecordAccountConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CheckRecordAccountConfig::getStoreId, storeId)
                .eq(CheckRecordAccountConfig::getStatus, 1)
                .eq(CheckRecordAccountConfig::getPlatformCode, code);
        return getOne(wrapper);
    }

    @Override
    public boolean changeStatus(String id, Integer status) {
        CheckRecordAccountConfig config = new CheckRecordAccountConfig();
        config.setId(id);
        config.setStatus(status);
        return updateById(config);
    }

    @Override
    public boolean saveConfig(CheckRecordAccountConfig config) {

        if (StringUtils.isBlank(config.getPlatformUsername())
                || StringUtils.isBlank(config.getPlatformUrl())
                || StringUtils.isBlank(config.getPlatformStoreName())
                || StringUtils.isBlank(config.getPlatformCode())
                || (config.getId() == null && StringUtils.isBlank(config.getPlatformPassword()))){
            throw new SportException("必填参数不能为空");
        }
        if (StringUtils.isNotBlank(config.getNewPlatformPassword())
                || StringUtils.isNotBlank(config.getPlatformPassword())){
            if (!config.getNewPlatformPassword().equals(config.getPlatformPassword())){
                throw new SportException("确认密码和密码不一致！");
            }
            config.setPlatformPassword(EncryptionUtil.encryptWithAES(config.getPlatformPassword(),EncryptionUtil.AES_KEY));
        }
        if (config.getId() != null && StringUtils.isBlank(config.getPlatformPassword())) {
            CheckRecordAccountConfig CheckRecordAccountConfig = getById(config.getId());
            config.setPlatformPassword(CheckRecordAccountConfig.getPlatformPassword());
        }

        //校验账号密码是否有效
        SyncDataAccountConfig config1 = new SyncDataAccountConfig();
        BeanUtils.copyProperties(config, config1);
//        boolean b = lhdSyncData.verifyAccount(config1, null);
//        if (!b){
//            throw new SportException("平台账号密码校验失败");
//        }
//        //校验门店名称是否存在，获取门店id并保存
//        String platformStoreId = lhdSyncData.getPlatformStoreId(config1);

//        if (StringUtils.isBlank(platformStoreId)){
//            throw new SportException("门店名称不一致，校验失败");
//        }
//        config.setPlatformStoreId(platformStoreId);

        if (config.getId() != null) {
            return this.updateById(config);
        } else {
            return this.save(config);
        }
    }
}
