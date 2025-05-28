package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.InvoiceRequest;
import com.aioveu.entity.InvoiceRequestDetail;

import java.util.List;

public interface InvoiceRequestDetailService extends IService<InvoiceRequestDetail> {

    List<InvoiceRequestDetail> getByInvoiceRequestId(Long invoiceRequestId);
}
