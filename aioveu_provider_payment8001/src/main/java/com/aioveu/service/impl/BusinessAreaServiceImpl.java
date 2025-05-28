package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.BusinessAreaDao;
import com.aioveu.entity.BusinessArea;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.entity.Region;
import com.aioveu.exception.SportException;
import com.aioveu.service.BusinessAreaService;
import com.aioveu.utils.RedisUtil;
import com.aioveu.vo.BusinessAreaConditionVO;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class BusinessAreaServiceImpl extends ServiceImpl<BusinessAreaDao, BusinessArea> implements BusinessAreaService {

    public static final String REDIS_BUSINESS_AREA_KEY = "SPORT_BUSINESS_AREA_";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BusinessAreaDao businessAreaDao;

    @Override
    public List<IdNameVO> getByRegionId(Long regionId) {
        return list(new QueryWrapper<BusinessArea>()
                .eq("region_id", regionId)
                .eq("status", 1)
                .orderByAsc("priority")
        )
                .stream()
                .map(item -> new IdNameVO(item.getId(), item.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public IPage<BusinessAreaConditionVO> getBusinessAreaListByCondition(int page, int size, String name, Integer id, Integer parentId) {
        return businessAreaDao.getBusinessAreaListByCondition(new Page<>(page, size), name, id, parentId);
    }

    @Override
    public Long getByName(String name) {
        String key = REDIS_BUSINESS_AREA_KEY + name;
        BusinessArea businessArea = redisUtil.get(key);
        if (businessArea == null) {
            QueryWrapper<BusinessArea> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            businessArea = getOne(queryWrapper);
            if (businessArea == null) {
                return null;
            }
            redisUtil.set(key, businessArea);
        }
        return businessArea.getId();
    }

    @Override
    public List<BusinessArea> getById(Long id) {
        QueryWrapper<BusinessArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BusinessArea::getRegionId, id);
        return list(queryWrapper);
    }

    @Override
    public Integer getRegionContainBusinessAreaCount(long id) {
        LambdaQueryWrapper<BusinessArea> selCondition = Wrappers.lambdaQuery();
        selCondition.eq(BusinessArea::getRegionId, id);
        Integer regionExist = businessAreaDao.selectCount(selCondition);
        return regionExist;
    }

    @Override
    public Integer addBusinessArea(BusinessArea businessArea) {
        if (StringUtils.isEmpty(businessArea.getRegionId())) {
            throw new SportException("所属区域不能为空");
        }
        if (StringUtils.isEmpty(businessArea.getName())) {
            throw new SportException("区域信息错误");
        }
        return businessAreaDao.insert(businessArea);
    }

    @Override
    public Integer modifyBusinessAreaMessage(BusinessArea businessArea) {
        LambdaUpdateWrapper<BusinessArea> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(BusinessArea::getId, businessArea.getId());
        int update = businessAreaDao.update(businessArea, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该城市");
        }
        return update;
    }

    @Override
    public Integer deleteBusinessArea(long id) {
        LambdaQueryWrapper<BusinessArea> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BusinessArea::getId, id);
        return businessAreaDao.delete(wrapper);
    }
}
