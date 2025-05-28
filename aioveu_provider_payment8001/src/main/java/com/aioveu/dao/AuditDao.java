package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Audit;
import com.aioveu.entity.StoreCoach;
import com.aioveu.vo.CoachTagVO;
import com.aioveu.vo.StoreCoachVO;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditDao extends BaseMapper<Audit> {

    Audit findCancelGradeAudit(Long gradeId, String userId);
}
