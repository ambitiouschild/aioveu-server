package com.aioveu.excel.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/20 0020 21:44
 */
@Data
public class UserInfoCallBean {

    @ExcelProperty(index = 0)
    private String channelCategory;

    @ExcelProperty(index = 1)
    private String channelName;

    @ExcelProperty(index = 2)
    private String phone;

    @ExcelProperty(index = 3)
    private String name;

    @ExcelProperty(index = 4)
    private String provinceName;

    @ExcelProperty(index = 5)
    private String cityName;

    @ExcelProperty(index = 6)
    private String childName;

    @ExcelProperty(index = 7)
    private String childSex;

    @ExcelProperty(index = 8)
    private Integer childAge;

    @ExcelProperty(index = 9)
    private String childInterestCategory;

    @ExcelProperty(index = 10)
    private String childSportBaseRemark;

    @ExcelProperty(index = 11)
    private String businessAreaName;

    @ExcelProperty(index = 12)
    private String regionName;

    @ExcelProperty(index = 13)
    private String address;

    @ExcelProperty(index = 14)
    private String groupName;

    private Long companyId;

}
