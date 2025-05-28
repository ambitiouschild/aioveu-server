package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CoachTagDao;
import com.aioveu.entity.CoachTag;
import com.aioveu.service.CategoryService;
import com.aioveu.service.CoachTagService;
import com.aioveu.vo.CategoryBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CoachTagServiceImpl extends ServiceImpl<CoachTagDao, CoachTag> implements CoachTagService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean createOrUpdateTag(String tags, Long coachId) {
        QueryWrapper<CoachTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CoachTag::getCoachId, coachId);
        remove(queryWrapper);

        Map<String, CategoryBaseVO> categoryMap = categoryService.getCategoryListByCode("store").stream().collect(Collectors.toMap(
                CategoryBaseVO::getName,
                Function.identity()));

        String[] tagArray = tags.split(",");
        List<CoachTag> coachTagList = new ArrayList<>();
        for (String item : tagArray) {
            CoachTag coachTag = new CoachTag();
            coachTag.setCoachId(coachId);
            coachTag.setName(item);
            if (categoryMap.get(item) != null) {
                coachTag.setIcon(categoryMap.get(item).getCover());
            }
            coachTagList.add(coachTag);
        }
        return saveBatch(coachTagList);
    }
}
