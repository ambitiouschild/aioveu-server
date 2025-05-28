package com.aioveu.vo;


import lombok.Data;

import java.util.Date;

@Data
public class WaterPoolSaleGroupUserVO extends BaseNameVO{

    private Long saleGroupId;

    private String userId;

    private String groupName;

    private String userName;


}
