package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CoachCertificateDao;
import com.aioveu.entity.CoachCertificate;
import com.aioveu.service.CoachCertificateService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CoachCertificateServiceImpl extends ServiceImpl<CoachCertificateDao, CoachCertificate> implements CoachCertificateService {

    @Override
    public List<CoachCertificate> getByCoachId(Long coachId) {
        QueryWrapper<CoachCertificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CoachCertificate::getCoachId, coachId);
        List<CoachCertificate> coachCertificates = list(queryWrapper);
        coachCertificates.forEach(item -> {
            item.setCertificate(FileUtil.getImageFullUrl(item.getCertificate()));
        });
        return coachCertificates;
    }

    @Override
    public Boolean batchAdd(List<CoachCertificate> coachCertificate) {
        return saveBatch(coachCertificate);
    }

    @Override
    public boolean deleteCertificate(Long id) {
        CoachCertificate coachCertificate = getById(id);
        if (coachCertificate != null) {
            OssUtil.deleteFile(coachCertificate.getCertificate());
        }
        return removeById(id);
    }

}
