package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Audit;
import com.aioveu.enums.AuditStatus;
import com.aioveu.enums.AuditType;
import com.aioveu.form.StoreAnalysisForm;
import com.aioveu.vo.AnalysisCouponVO;
import com.aioveu.vo.AnalysisOrderVO;
import com.aioveu.vo.StoreAnalysisVO;
import com.aioveu.vo.StoreCheckCouponItemVO;

import java.util.List;

public interface AuditService {

    IPage<Audit> selAuditByCondition(int page, int size, Integer storeId, String userId, Integer auditStatus, Integer auditType);

    void submit(Audit audit);

    void approved(Audit audit);

    void reject(Audit audit);
}
