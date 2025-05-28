package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CompanyDao;
import com.aioveu.entity.Company;
import com.aioveu.entity.StoreConfig;
import com.aioveu.entity.User;
import com.aioveu.enums.DataStatus;
import com.aioveu.form.CompanySettingForm;
import com.aioveu.service.CompanyService;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.service.StoreConfigService;
import com.aioveu.service.UserService;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.api.CompanyItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CompanyServiceImpl extends ServiceImpl<CompanyDao, Company> implements CompanyService {

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Override
    public IPage<CompanyItemVO> getAll(int page, int size, Integer status, String keyword) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Company::getStatus, status);
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.like("name", keyword);
        }
        queryWrapper.orderByAsc("create_date");
        IPage<Company> companyIPage = page(new Page<>(page, size), queryWrapper);

        List<CompanyItemVO> records = companyIPage.getRecords().stream().map(item -> {
            CompanyItemVO companyItemVO = new CompanyItemVO();
            BeanUtils.copyProperties(item, companyItemVO);
            return companyItemVO;
        }).collect(Collectors.toList());

        IPage<CompanyItemVO> iPage = new Page<>();
        BeanUtils.copyProperties(companyIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        Company company = new Company();
        company.setId(id);
        company.setStatus(status);
        return saveOrUpdate(company);
    }

    @Override
    public CompanySettingForm getCompanySettingForm(Long id) {
        CompanySettingForm form = new CompanySettingForm();
        Company company = getById(id);
        BeanUtils.copyProperties(company, form);
        StoreConfig appointmentCoachLimitConfig = storeConfigService.getCompanyStoreConfig(id, "appointment_coach_limit");
        if (appointmentCoachLimitConfig != null) {
            form.setAppointmentCoachLimit(Boolean.parseBoolean(appointmentCoachLimitConfig.getValue()) ? 1 : 0);
        }
        StoreConfig cancelBookingDaysConfig = storeConfigService.getCompanyStoreConfig(id, "cancel_booking_hours");
        if (cancelBookingDaysConfig != null) {
            form.setCancelBookingDays(Integer.valueOf(cancelBookingDaysConfig.getValue()));
        }
        return form;
    }

    @Override
    public boolean updateCompanySetting(CompanySettingForm data) {
        Company company = new Company();
        company.setId(data.getId());
        if (data.getAppointmentCoachLimit() != null) {
            storeConfigService.updateCompanyStoreConfig(data.getId(), "appointment_coach_limit", data.getAppointmentCoachLimit() == 1 ? "true":"false");
        }
        if (data.getCancelBookingDays() != null) {
            storeConfigService.updateCompanyStoreConfig(data.getId(), "cancel_booking_hours", data.getCancelBookingDays() + "");
        }
        company.setBeforeBookingMinutes(data.getBeforeBookingMinutes());
        company.setBrandLogo(data.getBrandLogo());
        company.setBrandName(data.getBrandName());
        company.setCancelGradeMinutes(data.getCancelGradeMinutes());
        company.setInvoiceContents(data.getInvoiceContents());
        company.setFieldBookNums(data.getFieldBookNums());
        return saveOrUpdate(company);
    }

    @Override
    public String getMiniAppPayIdById(Long companyId) {
        return getById(companyId).getMiniAppPayId();
    }

    @Override
    public Company getCompanyByMiniAppId(String appId) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Company::getMiniAppId, appId);
        List<Company> list = list(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Long getIdByAppId(String miniAppId) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Company::getMiniAppId, miniAppId);
        Company company = getOne(queryWrapper);
        if (company != null) {
            return company.getId();
        }
        return null;
    }

    @Override
    public List<CompanyItemVO> getUserValidCompanys(String username) {
        List<CompanyItemVO> records = new ArrayList<>();
        User user = userService.findUserByUsername(username);
        if(user == null) {
            return records;
        }
        List<StoreSimpleVO> stores = companyStoreUserService.getStoreByUserId(user.getId());

        if (CollectionUtils.isEmpty(stores)){
            return records;
        }
        List<Long> companyIds = stores.stream().map(StoreSimpleVO::getCompanyId).distinct().collect(Collectors.toList());

        QueryWrapper<Company> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Company::getStatus, DataStatus.NORMAL.getCode())
                .in(Company::getId, companyIds);
        List<Company> companyList = list(wrapper);

        records = companyList.stream().map(item -> {
            CompanyItemVO companyItemVO = new CompanyItemVO();
            BeanUtils.copyProperties(item, companyItemVO);
            return companyItemVO;
        }).collect(Collectors.toList());

        return records;
    }

    @Override
    public Long getCompanyIdByAppId(String appId) {
        Company company = getCompanyByMiniAppId(appId);
        if (company != null) {
            return company.getId();
        }
        return null;
    }

    @Override
    public Company getOneByStoreId(Long storeId) {
        return baseMapper.getOneByStoreId(storeId);
    }
}
