package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.InvoiceRequestDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRequestDetailDao extends BaseMapper<InvoiceRequestDetail> {
    List<InvoiceRequestDetail> getByInvoiceRequestId(Long invoiceRequestId);
}
