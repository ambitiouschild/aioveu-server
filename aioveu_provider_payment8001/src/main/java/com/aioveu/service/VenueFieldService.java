package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.VenueField;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.vo.VenueFieldVO;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface VenueFieldService extends IService<VenueField> {

    /**
     * 根据日期查询场馆的场地
     *
     * @param venueId
     * @param day
     * @return
     */
    List<VenueFieldVO> getFieldByVenueId(Long venueId, Long companyId, String day);

    List<VenueFieldVO> getFieldByVenueId(Long venueId, String day);

    List<VenueField> findByVenueId(Long venueId);

    /**
     * 生成场地计划
     *
     * @param now
     */
    void createFieldPlan(Date now) throws Exception;


    /**
     * 获取场馆有效场地
     * @param form
     * @return
     */
    List<VenueField> getActiveVenueFieldList(ActiveVenueFieldForm form);

    /**
     * 根据门店id，获取所有正在使用的场地、场馆信息
     * @param storeId
     * @return
     */
    List<VenueFieldVO> getFieldVenuesByStoreId(Long storeId);

    /**
     * id获取场地名称
     * @param id
     * @return
     */
    String getNameById(Long id);

    /**
     * 通过时间类型获取日期列表
     * @param formDateList
     * @param timeType
     * @return
     */
    List<String> getDateListByTimeType(List<String> formDateList, Integer timeType);

}
