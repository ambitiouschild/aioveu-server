package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CityDao;
import com.aioveu.entity.BusinessArea;
import com.aioveu.entity.City;
import com.aioveu.entity.Province;
import com.aioveu.entity.Region;
import com.aioveu.exception.SportException;
import com.aioveu.service.BusinessAreaService;
import com.aioveu.service.CityService;
import com.aioveu.service.RegionService;
import com.aioveu.utils.RedisUtil;
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
public class CityServiceImpl extends ServiceImpl<CityDao, City> implements CityService {

    public static final String REDIS_CITY_KEY = "SPORT_CITY_";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RegionService regionService;

    @Override
    public List<IdNameVO> getByProvinceId(Long provinceId) {
        return list(new QueryWrapper<City>()
                .eq("province_id", provinceId)
                .eq("status", 1)
                .orderByAsc("priority")
        )
                .stream()
                .map(item -> new IdNameVO(item.getId(), item.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public IPage<City> getCityListByCondition(int page, int size, String name, Integer id,Integer parentId) {
        LambdaQueryWrapper<City> wrapper = Wrappers.lambdaQuery();
        if (name != null) {
            wrapper.like(City::getName, name);
        }
        if (parentId != null) {
            wrapper.eq(City::getProvinceId, parentId);
        }
        if (id != null) {
            wrapper.eq(City::getId, id);
        }
        return cityDao.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Long getByName(String name) {
        String key = REDIS_CITY_KEY + name;
        City city = redisUtil.get(key);
        if (city == null) {
            QueryWrapper<City> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            city = getOne(queryWrapper);
            if (city == null) {
                return null;
            }
            redisUtil.set(key, city);
        }
        return city.getId();
    }

    @Override
    public Integer getProvinceContainCityCount(long id) {
        LambdaQueryWrapper<City> selCityCondition = Wrappers.lambdaQuery();
        selCityCondition.eq(City::getProvinceId, id);
        Integer cityExist = cityDao.selectCount(selCityCondition);
        return cityExist;
    }

    @Override
    public Integer addCity(City city) {
        if (StringUtils.isEmpty(city.getProvinceId())) {
            throw new SportException("所属省份不能为空");
        }
        if (StringUtils.isEmpty(city.getName())) {
            throw new SportException("城市信息错误");
        }
        return cityDao.insert(city);
    }

    @Override
    public Integer modifyCityMessage(City city) {
        LambdaUpdateWrapper<City> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(City::getId, city.getId());
        int update = cityDao.update(city, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该城市");
        }
        return update;
    }

    @Override
    public Integer deleteCity(long id) {
        Integer regionCount = regionService.getCityContainRegionCount(id);
        if (regionCount > 0) {
            throw new SportException("该城市下有区域信息，无法删除");
        }
        LambdaQueryWrapper<City> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(City::getId, id);
        return cityDao.delete(wrapper);
    }


}
