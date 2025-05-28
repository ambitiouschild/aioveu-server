package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GroupLuckDao;
import com.aioveu.entity.GroupLuck;
import com.aioveu.exception.SportException;
import com.aioveu.service.GroupLuckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GroupLuckServiceImpl extends ServiceImpl<GroupLuckDao, GroupLuck> implements GroupLuckService {

    @Override
    public boolean createData() {
        List<GroupLuck> list = new ArrayList<>();
        for (int i = 0; i < 16; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-25", "yyyy-MM-dd"));
                groupLuck.setGroupCode("s" + (i + 1));
                groupLuck.setName("少年组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 16; i < 20; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-25", "yyyy-MM-dd"));
                groupLuck.setGroupCode("y" + (i + 1));
                groupLuck.setName("幼儿组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 20; i < 30; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-26", "yyyy-MM-dd"));
                groupLuck.setGroupCode("y" + (i + 1));
                groupLuck.setName("幼儿组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 30; i < 41; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-26", "yyyy-MM-dd"));
                groupLuck.setGroupCode("x" + (i + 1));
                groupLuck.setName("学前组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 41; i < 63; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-27", "yyyy-MM-dd"));
                groupLuck.setGroupCode("x" + (i + 1));
                groupLuck.setName("学前组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 63; i < 83; i ++) {
            GroupLuck groupLuck = new GroupLuck();
            try {
                groupLuck.setGroupDate(DateUtils.parseDate("2021-05-28", "yyyy-MM-dd"));
                groupLuck.setGroupCode("x" + (i + 1));
                groupLuck.setName("学前组");
                list.add(groupLuck);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        log.info("数量:" + list.size());
        return saveBatch(list);
    }

    @Override
    public synchronized GroupLuck luck(String uuid, String groupName, String username) {
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(groupName) || StringUtils.isEmpty(username)) {
            throw new SportException(groupName + "参数错误, 不能为空！");
        }
        QueryWrapper<GroupLuck> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        GroupLuck groupLuck = getOne(queryWrapper);
        if (groupLuck != null) {
            return groupLuck;
        }
        QueryWrapper<GroupLuck> luckWrapper = new QueryWrapper<>();
        luckWrapper.eq("name", groupName);
        luckWrapper.isNull("uuid");
        List<GroupLuck> todoLuckList = list(luckWrapper);
        if (CollectionUtils.isEmpty(todoLuckList)) {
            throw new SportException(groupName + "抽奖已完毕, 请选其他组！");
        }
        Collections.shuffle(todoLuckList);
        groupLuck = todoLuckList.get(0);
        groupLuck.setUsername(username);
        groupLuck.setUuid(uuid);
        if (!saveOrUpdate(groupLuck)) {
            throw new SportException("系统错误, 请稍后重试！");
        }
        return groupLuck;
    }
}
