package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
public class InvoiceRequestOrderForm {

    private Long companyId;

    private String start;

    private String end;

}
