package com.aioveu.vo;

import com.aioveu.entity.Topic;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/10/31 9:52
 */
@Data
public class NearbyStoreVO extends IdNameVO {

    private String logo;

    private String address;

    private Double longitude;

    private Double latitude;

    private Double distance;

    private List<Topic> topicList;

    private String storeLogo;

}
