package com.aioveu.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SportStoreFieldNewVO {

    private Integer companyId;

    private Integer storeId;

    @NotEmpty(message = "场地信息不能为空")
    private List<String> storeFieldNameList;

}
