package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.BusinessArea;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.entity.Region;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CityService extends IService<City> {


    /**
     * 根据省份id获取下面的城市
     * @param provinceId
     * @return
     */
    List<IdNameVO> getByProvinceId(Long provinceId);

    /**
     * 根据条件获取城市信息
     * @param page
     * @param size
     * @param name
     * @param id
     * @return
     */
    IPage<City> getCityListByCondition(int page, int size, String name, Integer id,Integer parentId);

    /**
     * 根据名称获取id
     * @param name
     * @return
     */
    Long getByName(String name);

    /**
     * 获取省份内是否有包含城市信息
     * @param id
     * @return
     */
    Integer getProvinceContainCityCount(long id);


    /**
     * 新增城市信息
     * @param city
     * @return
     */
    Integer addCity(City city);

    /**
     * 修改城市信息
     * @param city
     * @return
     */
    Integer modifyCityMessage(City city);

    /**
     * 删除城市
     * @param id
     * @return
     */
    Integer deleteCity(long id);



}
