package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.RegionDao;
import com.aioveu.entity.Region;
import com.aioveu.exception.SportException;
import com.aioveu.service.BusinessAreaService;
import com.aioveu.service.CityService;
import com.aioveu.service.RegionService;
import com.aioveu.utils.RedisUtil;
import com.aioveu.vo.IdNameVO;
import com.aioveu.vo.RegionConditionVO;
import com.aioveu.vo.RegionVO;
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
public class RegionServiceImpl extends ServiceImpl<RegionDao, Region> implements RegionService {

    public static final String REDIS_REGION_KEY = "SPORT_REGION_";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RegionDao regionDao;

    @Autowired
    private BusinessAreaService businessAreaService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CityService cityServicey;

    @Override
    public List<IdNameVO> getByCityId(Long cityId) {
        return list(new QueryWrapper<Region>()
                .eq("city_id", cityId)
                .eq("status", 1)
                .orderByAsc("priority")
        )
                .stream()
                .map(item -> new IdNameVO(item.getId(), item.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public IPage<RegionConditionVO> getRegionListByCondition(int page, int size, String name, Integer id, Integer parentId) {
        return regionDao.getRegionListByCondition(new Page<>(page, size), name, id, parentId);
    }

    @Override
    public List<RegionVO> findAllByCityId(Long cityId) {
        return getBaseMapper().findAllByCityId(cityId);
    }

    @Override
    public Long getByName(String name) {
        String key = REDIS_REGION_KEY + name;
        Region region = redisUtil.get(key);
        if (region == null) {
            QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            region = getOne(queryWrapper);
            if (region == null) {
                return null;
            }
            redisUtil.set(key, region);
        }
        return region.getId();
    }

    @Override
    public Integer getCityContainRegionCount(long id) {
        LambdaQueryWrapper<Region> selCondition = Wrappers.lambdaQuery();
        selCondition.eq(Region::getCityId, id);
        Integer regionExist = regionDao.selectCount(selCondition);
        return regionExist;
    }

    @Override
    public Integer addRegion(Region region) {
        if (StringUtils.isEmpty(region.getCityId())) {
            throw new SportException("所属城市不能为空");
        }
        if (StringUtils.isEmpty(region.getName())) {
            throw new SportException("城市信息错误");
        }
        return regionDao.insert(region);
    }

    @Override
    public Integer modifyRegionMessage(Region region) {
        LambdaUpdateWrapper<Region> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Region::getId, region.getId());
        int update = regionDao.update(region, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该区域");
        }
        return update;
    }

    @Override
    public Integer deleteRegion(long id) {
        Integer businessAreaCount = businessAreaService.getRegionContainBusinessAreaCount(id);
        if (businessAreaCount > 0) {
            throw new SportException("该区域下有商圈信息，无法删除");
        }
        LambdaQueryWrapper<Region> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Region::getId, id);
        return regionDao.delete(wrapper);
    }

}
