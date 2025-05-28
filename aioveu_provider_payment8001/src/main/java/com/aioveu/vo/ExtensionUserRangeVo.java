package com.aioveu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExtensionUserRangeVo {

    private String themeName;

    private String extensionName;

    private Double longitude;

    private Double latitude;

    private Integer distance;

    private String logoUrl;
}
