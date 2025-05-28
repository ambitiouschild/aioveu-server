package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.InvoiceRequestDao;
import com.aioveu.entity.InvoiceRequest;
import com.aioveu.entity.InvoiceRequestDetail;
import com.aioveu.enums.InvoiceRequestStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.InvoiceRequestDetailService;
import com.aioveu.service.InvoiceRequestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class InvoiceRequestServiceImpl extends ServiceImpl<InvoiceRequestDao, InvoiceRequest> implements InvoiceRequestService {

    @Autowired
    private InvoiceRequestDetailService invoiceRequestDetailService;

    @Override
    public IPage<InvoiceRequest> getByCompanyId(Long companyId, String start, String end, String phone, Integer status, Integer page, Integer size) {
        if (!StringUtils.isEmpty(end)) {
            end += " 23:59:59";
        }
        IPage<InvoiceRequest> invoiceRequestIPage = this.baseMapper.getByCompanyId(new Page<>(page, size), start, end, companyId, phone, status);
        for (InvoiceRequest record : invoiceRequestIPage.getRecords()) {
            List<InvoiceRequestDetail> invoiceRequestDetailList = invoiceRequestDetailService.getByInvoiceRequestId(record.getId());
            record.setInvoiceRequestDetailList(invoiceRequestDetailList);
        }
        return invoiceRequestIPage;
    }

    @Override
    public void submit(InvoiceRequest request) {
        BigDecimal amount = BigDecimal.ZERO;
        for (InvoiceRequestDetail invoiceRequestDetail : request.getInvoiceRequestDetailList()) {
            List<InvoiceRequest> byOrderId = this.baseMapper.getByOrderId(invoiceRequestDetail.getOrderId());
            if (byOrderId.size() > 0) {
                throw new SportException(String.format("订单号%s已经提交开票或已开票，请检查", invoiceRequestDetail.getOrderId()));
            }
            amount = amount.add(invoiceRequestDetail.getOrderAmount());
        }
        request.setInvoiceAmount(amount);
        request.setStatus(InvoiceRequestStatus.Submit.getCode());
        this.baseMapper.insert(request);
        for (InvoiceRequestDetail invoiceRequestDetail : request.getInvoiceRequestDetailList()) {
            invoiceRequestDetail.setInvoiceRequestId(request.getId());
            this.invoiceRequestDetailService.getBaseMapper().insert(invoiceRequestDetail);
        }
    }

    @Override
    public void approved(InvoiceRequest request) {
        InvoiceRequest invoiceRequest = this.baseMapper.selectById(request.getId());
        if (!invoiceRequest.getStatus().equals(InvoiceRequestStatus.Submit.getCode())) {
            throw new RuntimeException("状态必须为开票中，才可以操作");
        }
        request.setIssuerDate(new Date());
        request.setStatus(InvoiceRequestStatus.Approved.getCode());
        this.baseMapper.updateById(request);
    }

    @Override
    public void reject(InvoiceRequest request) {
        InvoiceRequest invoiceRequest = this.baseMapper.selectById(request.getId());
        if (!invoiceRequest.getStatus().equals(InvoiceRequestStatus.Submit.getCode())) {
            throw new RuntimeException("状态必须为开票中，才可以操作");
        }
        request.setIssuerDate(new Date());
        request.setStatus(InvoiceRequestStatus.Reject.getCode());
        this.baseMapper.updateById(request);
    }
}
