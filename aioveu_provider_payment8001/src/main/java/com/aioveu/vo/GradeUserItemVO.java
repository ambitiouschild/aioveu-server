package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeUserItemVO {

    private Long id;

    private String name;

    private Date startTime;

    private Date endTime;

    private String storeName;

    private Integer limitNumber;

    private Integer enrollNumber;

    private List<StoreSimpleCoachVO> coachList;

    private String day;

}
