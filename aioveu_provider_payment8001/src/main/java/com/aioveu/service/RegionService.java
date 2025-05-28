package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.City;
import com.aioveu.entity.Region;
import com.aioveu.vo.IdNameVO;
import com.aioveu.vo.RegionConditionVO;
import com.aioveu.vo.RegionVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RegionService extends IService<Region> {


    /**
     * 根据省份id获取下面的城市
     * @param cityId
     * @return
     */
    List<IdNameVO> getByCityId(Long cityId);

    /**
     * 根据条件获取区域信息
     * @param page
     * @param size
     * @param name
     * @param id
     * @return
     */
    IPage<RegionConditionVO> getRegionListByCondition(int page, int size, String name, Integer id, Integer parentId);


    /**
     * 查找当前城市下的所有区域
     * @param cityId
     * @return
     */
    List<RegionVO> findAllByCityId(Long cityId);

    /**
     * 根据名称获取id
     * @param name
     * @return
     */
    Long getByName(String name);

    /**
     * 获取城市内是否有包含区域信息
     * @param id
     * @return
     */
    Integer getCityContainRegionCount(long id);


    /**
     * 新增区域
     * @param region
     * @return
     */
    Integer addRegion(Region region);

    /**
     * 修改区域信息
     * @param region
     * @return
     */
    Integer modifyRegionMessage(Region region);

    /**
     * 删除区域
     * @param id
     * @return
     */
    Integer deleteRegion(long id);


}
