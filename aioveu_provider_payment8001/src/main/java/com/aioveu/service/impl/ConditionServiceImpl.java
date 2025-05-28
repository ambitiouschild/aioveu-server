package com.aioveu.service.impl;

import com.aioveu.service.CategoryService;
import com.aioveu.service.CityService;
import com.aioveu.service.ConditionService;
import com.aioveu.service.RegionService;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.BusinessAreaVO;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.RegionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/17 0017 18:27
 */
@Service
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CityService cityService;

    @Override
    public Map<String, Object> getExerciseMenu(String city) {
        Long cityId = cityService.getByName(city);
        return getServiceListMenu(cityId, 3);
    }

    @Override
    public Map<String, Object> getStoreMenu(String city) {
        Long cityId = cityService.getByName(city);
        return getServiceListMenu(cityId, 4);
    }

    private Map<String, Object> getServiceListMenu(Long cityId, int type) {
        Map<String, Object> filterMap = new HashMap<>();
        List<RegionVO> regionList = regionService.findAllByCityId(cityId);
        regionList.forEach(item -> {
            List<BusinessAreaVO> childList = item.getChildList();
            childList.add(0, createBusinessArea(-1L, "全部", -1L));
        });

        if (type == 3) {
            List<Map<String, String>> timeList = SportDateUtils.get7Date();
            Map<String, String> oldTime = new HashMap<>();
            oldTime.put("name", "全部");
            oldTime.put("value", "null");
            timeList.add(0, oldTime);

//            if (type == 3) {
//
//            } else {
//                Map<String, String> oldTime = new HashMap<>();
//                oldTime.put("name", "往期");
//                oldTime.put("value", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
//                timeList.add(0, oldTime);
//            }
            filterMap.put("timeList", timeList);
        } else if (type == 4) {
            List<CategoryBaseVO> categoryList = categoryService.getCategoryListByCode("store");
            CategoryBaseVO cv = new CategoryBaseVO();
            cv.setId(-1L);
            cv.setName("全部");
            cv.setCode("ALL");
            categoryList.add(0, cv);
            filterMap.put("categoryList", categoryList);
        }

        regionList.add(0, createNearBy());
        filterMap.put("regionList", regionList);

        filterMap.put("sortList", createSort(type));
        return filterMap;
    }

    private RegionVO createNearBy() {
        RegionVO region = new RegionVO();
        region.setName("附近");
        region.setId(-10L);
        List<BusinessAreaVO> nearList = new ArrayList<>();
        nearList.add(createBusinessArea(-1L, "全城", region.getId()));
        nearList.add(createBusinessArea(500L, "500m", region.getId()));
        nearList.add(createBusinessArea(1000L, "1km", region.getId()));
        nearList.add(createBusinessArea(3000L, "3km", region.getId()));
        nearList.add(createBusinessArea(5000L, "5km", region.getId()));
        region.setChildList(nearList);
        return region;
    }

    private BusinessAreaVO createBusinessArea(Long id, String name, Long regionId) {
        BusinessAreaVO item = new BusinessAreaVO();
        item.setId(id);
        item.setName(name);
        item.setRegionId(regionId);
        return item;
    }

    private List<Map<String, String>> createSort(int type) {
        List<Map<String, String>> sortList = new ArrayList<>();
        Map<String, String> defaultMap = new HashMap<>();
        defaultMap.put("name", "排序");
        defaultMap.put("value", "null");

        Map<String, String> distanceMap = new HashMap<>();
        distanceMap.put("name", "离我最近");
        distanceMap.put("value", "distance");

        sortList.add(defaultMap);
        sortList.add(distanceMap);

        if (type != 4) {
            Map<String, String> priceMap = new HashMap<>();
            priceMap.put("name", "价格最低");

            if (type == 0) {
                Map<String, String> scoreMap = new HashMap<>();
                scoreMap.put("name", "评分最高");
                scoreMap.put("value", "T.`score` DESC");

                Map<String, String> viewMap = new HashMap<>();
                viewMap.put("name", "热度最高");
                viewMap.put("value", "T.`view_count` DESC");

                sortList.add(scoreMap);
                sortList.add(viewMap);

                priceMap.put("value", "T.`lower_price`");
            } else if (type == 1) {
                priceMap.put("value", "T.`join_money`");
            } else if (type == 2) {
                priceMap.put("value", "T.`lower_price`");
            } else if (type == 3) {
                priceMap.put("value", "T.`lower_price`");
            }
            sortList.add(priceMap);
        }
        if (type == 4) {
            Map<String, String> recommendMap = new HashMap<>();
            recommendMap.put("name", "优先推荐");
            recommendMap.put("value", "`T.recommend_order desc`");
            sortList.add(recommendMap);
        }
        return sortList;
    }
}
