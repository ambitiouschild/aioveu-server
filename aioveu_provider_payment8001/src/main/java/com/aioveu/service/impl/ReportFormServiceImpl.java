package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ReportFormDao;
import com.aioveu.entity.ReportForm;
import com.aioveu.service.ReportFormService;
import com.aioveu.service.UserService;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ReportFormServiceImpl extends ServiceImpl<ReportFormDao, ReportForm> implements ReportFormService {

    @Autowired
    private UserService userService;

    @Override
    public boolean create(ReportForm reportForm, String username) {
        UserVo userVo = userService.findByUsername(username);
        reportForm.setCreateUserId(userVo.getId());
        reportForm.setCreateUsername(userVo.getName());
        return save(reportForm);
    }

    @Override
    public IPage<ReportForm> getList(int page, int size, Long storeId, String username) {
        UserVo userVo = userService.findByUsername(username);
        QueryWrapper<ReportForm> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportForm::getStoreId, storeId);
        if (userVo.getRoles().contains("brush_user")) {

        } else {
            queryWrapper.lambda().eq(ReportForm::getCreateUserId, userVo.getId());
        }
        queryWrapper.lambda().orderByDesc(ReportForm::getCreateDate);
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean read(List<Long> ids) {
        List<ReportForm> reportForms = new ArrayList<>();
        for (Long id : ids) {
            ReportForm reportForm = new ReportForm();
            reportForm.setId(id);
            reportForm.setStatus(2);
            reportForms.add(reportForm);
        }
        return updateBatchById(reportForms);
    }

    @Override
    public boolean batchDelete(List<Long> ids, String username) {
        return removeByIds(ids);
    }
}
