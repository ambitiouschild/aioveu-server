package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.aioveu.vo.BaseNameVO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class GradeForm {

    private Long id;

    private String name;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 最低人数
     */
    private Integer minNumber;

    /**
     * 是否超额 默认不可
     */
    private Boolean exceed;

    /**
     * 上课教室id
     */
    private Long gradeClassroomId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date day;

    private String startTime;

    private String endTime;

    private String remark;

    private List<BaseNameVO> teacherList;

    @NotNull(message = "场地id不能为空")
    private Long venueId;

    private Integer sharedVenue;

    private List<Long> fieldIdList;
}
