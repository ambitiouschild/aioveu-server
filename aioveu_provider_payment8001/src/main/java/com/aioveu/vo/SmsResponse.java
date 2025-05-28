package com.aioveu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsResponse {

    @JsonProperty(value = "Message")
    private String Message;

    @JsonProperty(value = "RequestId")
    private String RequestId;

    @JsonProperty(value = "BizId")
    private String BizId;

    @JsonProperty(value = "Code")
    private String Code;

}
