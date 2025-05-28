package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaoyao
 * @Date 2022年10月10日
 */
@Data
@TableName("sport_user_recommend_record")
public class UserRecommendRecord extends IdEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 推荐人用户Id
     */
    private String recommendUserId;

}
