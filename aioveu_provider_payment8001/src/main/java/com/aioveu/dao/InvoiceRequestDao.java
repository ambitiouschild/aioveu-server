package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.InvoiceRequest;
import com.aioveu.vo.GradeVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRequestDao extends BaseMapper<InvoiceRequest> {
    IPage<InvoiceRequest> getByCompanyId(IPage<GradeVO> page, String start, String end, Long companyId, String phone, Integer status);
    List<InvoiceRequest> getByOrderId(String orderId);

}
