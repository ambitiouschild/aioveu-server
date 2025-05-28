package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aioveu.entity.*;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author: yeshuaibiao
 * @date: 2024/01/05 10:42
 */
@Slf4j
@Service
public class CancelGradeServiceImpl implements IApprovedService {

    @Autowired
    private GradeService gradeService;


    @Autowired
    private CompanyService companyService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private MQMessageService mqMessageService;

    @Override
    public boolean approved(String jsonVal) {
        GradeCancelRecord cancelRecord = JSONObject.parseObject(jsonVal, GradeCancelRecord.class);
        if (StringUtils.isEmpty(cancelRecord.getUserName()))
            cancelRecord.setUserName("15000699768");
        return gradeService.cancel(cancelRecord, cancelRecord.getUserName());
    }

    @Override
    public boolean reject(String jsonVal) {
        return true;
    }


    @Override
    public boolean submit(Audit audit) {
        JSONObject json = JSONObject.parseObject(audit.getJsonVal());
        Company company = companyService.getById(json.getString("companyId"));
        if (company == null){
            return false;
        }
        Grade grade = gradeService.getById(json.getString("gradeId"));
        if (grade == null){
            return false;
        }
        //判断提交取消班级申请，是否允许
        if (company.getCancelGradeMinutes() != null){
            Date now = new Date(new Date().getTime() + company.getCancelGradeMinutes()*60*1000);
            if (grade.getStartTime().before(now)){
                throw new SportException("失败:请至少提前"+company.getCancelGradeMinutes()+"分钟取消");
            }
        }
        try {
            Store store = storeService.getById(grade.getStoreId());
            //公众号通知
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("userId", audit.getCreateUserId());
            msgMap.put("storeName", store.getName());
            msgMap.put("gradeName", grade.getName());
            msgMap.put("gradeTime", DateFormatUtils.format(grade.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            msgMap.put("coachName", json.getString("name"));
            msgMap.put("reason", json.getString("explainReason"));
            mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.AUDIT_CANCEL_GRADE_MSG.getCode(), grade.getStoreId());
        }catch (Exception e){
            log.error("取消课程{},通知失败{}",audit.getJsonVal(), e.getMessage());
        }

        return true;
    }

}
