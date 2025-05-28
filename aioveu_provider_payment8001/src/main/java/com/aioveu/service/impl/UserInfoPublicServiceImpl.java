package com.aioveu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserInfoPublicDao;
import com.aioveu.entity.UserInfoBase;
import com.aioveu.entity.UserInfoPublic;
import com.aioveu.enums.DataStatus;
import com.aioveu.excel.bean.UserInfoCallBean;
import com.aioveu.excel.listener.UserInfoCallListener;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserInfoPublicServiceImpl extends ServiceImpl<UserInfoPublicDao, UserInfoPublic> implements UserInfoPublicService {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private BusinessAreaService businessAreaService;


    @Override
    public boolean importData(MultipartFile multipartFile) {
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            // 解析每行结果在listener中处理
            UserInfoCallListener listener = new UserInfoCallListener();
            listener.setUserInfoPublicService(this);
            EasyExcel.read(inputStream, UserInfoCallBean.class, listener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean batchSave(List<UserInfoCallBean> list, String userId) {
        if (CollectionUtils.isEmpty(list)) {
            throw new SportException("未解析到数据");
        }
        List<UserInfoPublic> userInfoList = new ArrayList<>();
        for (UserInfoCallBean item : list) {
            UserInfoPublic userInfo = new UserInfoPublic();
            BeanUtils.copyProperties(item, userInfo);
            if (StringUtils.isEmpty(item.getChildSex()) || item.getChildSex().equals("未知")) {
                userInfo.setChildGender(0);
            } else if (item.getChildSex().equals("男")) {
                userInfo.setChildGender(1);
            } else if (item.getChildSex().equals("女")) {
                userInfo.setChildGender(2);
            }
            if (StringUtils.isNotEmpty(userInfo.getProvinceName())) {
                userInfo.setProvinceId(provinceService.getByName(userInfo.getProvinceName()));
            }
            if (StringUtils.isNotEmpty(userInfo.getCityName())) {
                userInfo.setCityId(cityService.getByName(userInfo.getCityName()));
            }
            if (StringUtils.isNotEmpty(userInfo.getRegionName())) {
                userInfo.setRegionId(regionService.getByName(userInfo.getRegionName()));
            }
            if (StringUtils.isNotEmpty(userInfo.getBusinessAreaName())) {
                userInfo.setBusinessAreaId(businessAreaService.getByName(userInfo.getBusinessAreaName()));
            }
            userInfoList.add(userInfo);
        }
        return saveBatch(userInfoList);
    }

    @Override
    public IPage<UserInfoVO> getList(int page, int size) {
        QueryWrapper<UserInfoPublic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfoPublic::getStatus, DataStatus.NORMAL.getCode());
        IPage<UserInfoPublic> userInfoIPage = page(new Page<>(page, size), queryWrapper);

        List<UserInfoVO> records = userInfoIPage.getRecords().stream().map(item -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(item, userInfoVO);
            return userInfoVO;
        }).collect(Collectors.toList());

        IPage<UserInfoVO> iPage = new Page<>();
        BeanUtils.copyProperties(userInfoIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public UserInfoPublic getByPhone(String phone) {
        QueryWrapper<UserInfoPublic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfoPublic::getStatus, DataStatus.NORMAL.getCode())
                .eq(UserInfoBase::getPhone, phone);
        List<UserInfoPublic> userInfoPublicList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userInfoPublicList)) {
            return userInfoPublicList.get(0);
        }
        return null;
    }
}
