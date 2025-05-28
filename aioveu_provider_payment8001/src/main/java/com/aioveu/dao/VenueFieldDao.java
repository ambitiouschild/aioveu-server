package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.VenueField;
import com.aioveu.vo.VenueFieldVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Repository
public interface VenueFieldDao extends BaseMapper<VenueField> {

    /**
     * 根据日期查询场馆的场地信息
     * @param venueId
     * @param day
     * @return
     */
    List<VenueFieldVO> getFieldByVenueId(Long venueId, String day);


    /**
     * 根据门店id，获取所有正在使用的场地、场馆信息
     * @param storeId
     * @return
     */
    List<VenueFieldVO> getFieldVenuesByStoreId(Long storeId);
}
