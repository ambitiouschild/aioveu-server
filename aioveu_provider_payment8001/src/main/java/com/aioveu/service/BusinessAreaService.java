package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.BusinessArea;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.vo.BusinessAreaConditionVO;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface BusinessAreaService extends IService<BusinessArea> {


    /**
     * 根据省份id获取下面的城市
     * @param regionId
     * @return
     */
    List<IdNameVO> getByRegionId(Long regionId);

    /**
     * 根据条件获取商圈信息
     * @param page
     * @param size
     * @param name
     * @param id
     * @return
     */
    IPage<BusinessAreaConditionVO> getBusinessAreaListByCondition(int page, int size, String name, Integer id, Integer parentId);

    /**
     * 根据名称获取id
     * @param name
     * @return
     */
    Long getByName(String name);

    /**
     * 根据区域查询商圈列表
     * @return
     */
    List<BusinessArea> getById(Long id);

    /**
     * 获取省份内是否有包含城市信息
     * @param id
     * @return
     */
    Integer getRegionContainBusinessAreaCount(long id);

    /**
     * 新增商圈信息
     * @param businessArea
     * @return
     */
    Integer addBusinessArea(BusinessArea businessArea);

    /**
     * 修改商圈信息
     * @param businessArea
     * @return
     */
    Integer modifyBusinessAreaMessage(BusinessArea businessArea);

    /**
     * 删除商圈
     * @param id
     * @return
     */
    Integer deleteBusinessArea(long id);
}
