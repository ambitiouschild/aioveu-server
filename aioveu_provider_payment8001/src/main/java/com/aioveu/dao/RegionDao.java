package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.City;
import com.aioveu.entity.Region;
import com.aioveu.vo.RegionConditionVO;
import com.aioveu.vo.RegionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface RegionDao extends BaseMapper<Region> {

    /**
     * 查找当前城市下的所有区域
     * @param cityId
     * @return
     */
    List<RegionVO> findAllByCityId(Long cityId);

    /**
     * 根据条件查询区域信息
     * @param page
     * @param name
     * @param id
     * @param parentId
     * @return
     */
    IPage<RegionConditionVO> getRegionListByCondition(IPage<RegionConditionVO> page, String name, Integer id, Integer parentId);


}
