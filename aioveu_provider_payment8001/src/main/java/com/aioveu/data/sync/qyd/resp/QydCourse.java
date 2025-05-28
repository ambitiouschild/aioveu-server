package com.aioveu.data.sync.qyd.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 趣运动场地对象
 * @author: 雒世松
 * @date: 2025/3/27 0014 21:06
 */
@Data
public class QydCourse implements Serializable {

    private int course_id;

    private String course_name;

}