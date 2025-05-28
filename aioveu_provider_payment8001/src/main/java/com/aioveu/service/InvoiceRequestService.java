package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.InvoiceRequest;
import com.aioveu.form.InvoiceRequestForm;

import java.util.List;

public interface InvoiceRequestService extends IService<InvoiceRequest> {

    IPage<InvoiceRequest> getByCompanyId(Long companyId, String start, String end, String phone, Integer status, Integer page, Integer size);

    void submit(InvoiceRequest request);

    void approved(InvoiceRequest request);

    void reject(InvoiceRequest request);
}
