package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ProvinceDao;
import com.aioveu.entity.Province;
import com.aioveu.exception.SportException;
import com.aioveu.service.CityService;
import com.aioveu.service.ProvinceService;
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
public class ProvinceServiceImpl extends ServiceImpl<ProvinceDao, Province> implements ProvinceService {

    public static final String REDIS_PROVINCE_KEY = "SPORT_PROVINCE_";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProvinceDao provinceDao;

    @Autowired
    private CityService cityService;


    @Override
    public List<IdNameVO> getAll() {
        return list(new QueryWrapper<Province>()
                .eq("status", 1)
                .orderByAsc("priority")
        )
                .stream()
                .map(item -> new IdNameVO(item.getId(), item.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Long getByName(String name) {
        String key = REDIS_PROVINCE_KEY + name;
        Province province = redisUtil.get(key);
        if (province == null) {
            QueryWrapper<Province> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            province = getOne(queryWrapper);
            if (province == null) {
                return null;
            }
            redisUtil.set(key, province);
        }
        return province.getId();
    }

    @Override
    public IPage<Province> getProvinceListByCondition(int page, int size, String name, Integer id) {
        LambdaQueryWrapper<Province> wrapper = Wrappers.lambdaQuery();
        if (name != null) {
            wrapper.like(Province::getName, name);
        }
        if (id != null) {
            wrapper.eq(Province::getId, id);
        }
        return provinceDao.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Integer addProvince(Province province) {
        if (StringUtils.isEmpty(province.getName())) {
            throw new SportException("省份信息错误");
        }
        return provinceDao.insert(province);
    }

    @Override
    public Integer modifyProvinceMessage(Province province) {
        LambdaUpdateWrapper<Province> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Province::getId, province.getId());
        int update = provinceDao.update(province, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该省份");
        }
        return update;
    }

    @Override
    public Integer deleteProvince(long id) {
        Integer cityExist = cityService.getProvinceContainCityCount(id);
        if (cityExist > 0) {
            throw new SportException("该省份下有城市信息，无法删除");
        }
        LambdaQueryWrapper<Province> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Province::getId, id);
        return provinceDao.delete(wrapper);
    }
}
