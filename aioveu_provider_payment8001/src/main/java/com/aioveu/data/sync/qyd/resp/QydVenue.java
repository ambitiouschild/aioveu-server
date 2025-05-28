package com.aioveu.data.sync.qyd.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 趣运动场馆内部对象
 * @author: 雒世松
 * @date: 2025/3/27 16:07
 */
@Data
public class QydVenue implements Serializable {

    private int cat_id;

    private String cat_name;

    /**
     * 场地信息
     */
    private List<QydCourse> courses;
}
