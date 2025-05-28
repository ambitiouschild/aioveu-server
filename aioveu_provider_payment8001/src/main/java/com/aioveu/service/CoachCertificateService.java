package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CoachCertificate;

import java.util.List;

public interface CoachCertificateService extends IService<CoachCertificate> {

    /**
     * 根据教练id查询证书
     * @param coachId
     * @return
     */
    List<CoachCertificate> getByCoachId(Long coachId);

    /**
     * 添加教练证书
     * @param coachCertificate
     * @return
     */
    Boolean batchAdd(List<CoachCertificate> coachCertificate);

    /**
     * 根据证书id 教练id删除数据
     * @param id
     * @return
     */
    boolean deleteCertificate(Long id);



}
