package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Province;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ProvinceService extends IService<Province> {

    /**
     * 获取所有省份
     * @return
     */
    List<IdNameVO> getAll();

    /**
     * 根据名称获取id
     * @param name
     * @return
     */
    Long getByName(String name);

    /**
     * 根据条件获取省份信息
     * @param page
     * @param size
     * @param name
     * @param id
     * @return
     */
    IPage<Province> getProvinceListByCondition(int page, int size, String name, Integer id);


    /**
     * 添加省份
     * @param province
     * @return
     */
    Integer addProvince(Province province);

    /**
     * 修改省份信息
     * @param province
     * @return
     */
    Integer modifyProvinceMessage(Province province);

    /**
     * 删除省份
     * @param id
     * @return
     */
    Integer deleteProvince(long id);
}
