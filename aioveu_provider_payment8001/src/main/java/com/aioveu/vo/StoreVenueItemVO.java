package com.aioveu.vo;

import com.aioveu.entity.VenueField;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class StoreVenueItemVO {

    private Long id;

    private String name;

    private String logo;

    private String tags;

    private Integer status;

    private Boolean bookOpen;

    private String fields;

    private List<VenueField> venueFieldList;

}
