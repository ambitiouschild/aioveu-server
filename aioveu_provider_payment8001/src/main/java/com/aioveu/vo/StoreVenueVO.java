package com.aioveu.vo;

import com.aioveu.entity.VenueField;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StoreVenueVO {

    private Long id;

    @NotNull(message = "场馆不能为空")
    private String name;

    @NotNull(message = "公司id不能为空")
    private Long companyId;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private String logo;

    private String tags;

    private Boolean bookOpen;

    private String categoryCode;

    private Integer status;

    private List<VenueField> venueFieldList;

}
