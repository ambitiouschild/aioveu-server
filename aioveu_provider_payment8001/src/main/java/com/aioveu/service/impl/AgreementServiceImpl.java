package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.AgreementDao;
import com.aioveu.entity.Agreement;
import com.aioveu.entity.Company;
import com.aioveu.service.AgreementService;
import com.aioveu.service.CompanyService;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.AgreementDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementDao, Agreement> implements AgreementService {

    @Autowired
    private AgreementDao agreementDao;

    @Autowired
    private CompanyService companyService;


    @Override
    public IPage<AgreementDetailVO> getManagerAll(int page, int size, Long companyId) {
        return agreementDao.getManagerAll(new Page<>(page, size), companyId);
    }

    @Override
    public boolean deleteImage(Long id) {
        Agreement agreement = getById(id);
        if (agreement == null) {
            return false;
        }
        OssUtil.deleteFile(agreement.getUrl());
        return removeById(id);
    }

    @Override
    public AgreementDetailVO managerDetail(Long id) {
        Agreement agreement = getById(id);
        if (agreement != null) {
            AgreementDetailVO agreementDetailVO = new AgreementDetailVO();
            BeanUtils.copyProperties(agreement, agreementDetailVO);
            if (agreement.getCompanyId()!=null) {
                Company company = companyService.getById(agreement.getCompanyId());
                if (company != null) {
                    agreementDetailVO.setCompanyName(company.getName());
                }
            }
            return agreementDetailVO;
        }
        return null;
    }
}
