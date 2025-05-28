package com.aioveu.vo;

import lombok.Data;

/**
 * 做统计查询vo对象
 *
 * @Author： yao
 * @Date： 2025/4/26 13:38
 * @Describe：
 */
@Data
public class StatisticsVo extends IdNameVO{
    //统计总数
    Integer total;
}
