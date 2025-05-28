package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.ResultEnum;
import com.aioveu.dao.FormEnrollDao;
import com.aioveu.entity.FormEnroll;
import com.aioveu.exception.SportException;
import com.aioveu.form.FormEnrollForm;
import com.aioveu.service.EnrollQuestionService;
import com.aioveu.service.FormEnrollService;
import com.aioveu.vo.FormEnrollManagerItemVO;
import com.aioveu.vo.FormEnrollVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class FormEnrollServiceImpl extends ServiceImpl<FormEnrollDao, FormEnroll> implements FormEnrollService {

    @Autowired
    private EnrollQuestionService enrollQuestionService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(FormEnrollForm formEnrollForm) {
        FormEnroll formEnroll = new FormEnroll();
        BeanUtils.copyProperties(formEnrollForm, formEnroll);
        save(formEnroll);
        return enrollQuestionService.batchCreate(formEnrollForm.getQuestionFormList(), formEnroll.getId());
    }

    @Override
    public FormEnrollVO getDetail(Long id) {
        FormEnroll formEnroll = getById(id);
        if (formEnroll == null) {
            throw new SportException(ResultEnum.NOT_FOUND.getCode(), id + "不存在");
        }
        if (formEnroll.getStatus() == 0) {
            throw new SportException(ResultEnum.NOT_FOUND.getCode(), id + "不存在");
        }
        FormEnrollVO formEnrollVO = new FormEnrollVO();
        BeanUtils.copyProperties(formEnroll, formEnrollVO);
        formEnrollVO.setQuestionList(enrollQuestionService.getQuestionList(id));
        return formEnrollVO;
    }

    @Override
    public IPage<FormEnrollManagerItemVO> pageList(Integer page, Integer size) {
        QueryWrapper<FormEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("create_date");
        IPage<FormEnroll> formEnrollIPage = page(new Page<>(page, size), queryWrapper);

        List<FormEnrollManagerItemVO> records = formEnrollIPage.getRecords().stream().map(item -> {
            FormEnrollManagerItemVO formEnrollManagerItemVO = new FormEnrollManagerItemVO();
            BeanUtils.copyProperties(item, formEnrollManagerItemVO);
            return formEnrollManagerItemVO;
        }).collect(Collectors.toList());

        IPage<FormEnrollManagerItemVO> iPage = new Page<>();
        BeanUtils.copyProperties(formEnrollIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        FormEnroll formEnroll = new FormEnroll();
        formEnroll.setId(id);
        formEnroll.setStatus(status);
        return saveOrUpdate(formEnroll);
    }
}
