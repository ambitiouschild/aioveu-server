package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.InvoiceRequestDetailDao;
import com.aioveu.entity.InvoiceRequestDetail;
import com.aioveu.service.InvoiceRequestDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InvoiceRequestDetailServiceImpl extends ServiceImpl<InvoiceRequestDetailDao, InvoiceRequestDetail> implements InvoiceRequestDetailService {
    @Override
    public List<InvoiceRequestDetail> getByInvoiceRequestId(Long invoiceRequestId) {
        return this.baseMapper.getByInvoiceRequestId(invoiceRequestId);
    }
}
