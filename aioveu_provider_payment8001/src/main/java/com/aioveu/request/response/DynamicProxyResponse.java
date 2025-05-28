package com.aioveu.request.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/02/20 15:24
 */
@Data
public class DynamicProxyResponse {

    private Integer count;

    @JsonProperty("filter_count")
    private Integer filterCount;

    @JsonProperty("surplus_quantity")
    private Integer surplusQuantity;

    @JsonProperty("proxy_list")
    private List<DynamicProxyIP> proxyList;

}
