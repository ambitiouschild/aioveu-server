package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.InvoiceRequest;
import com.aioveu.form.InvoiceRequestOrderForm;
import com.aioveu.service.InvoiceRequestService;
import com.aioveu.service.OrderService;
import com.aioveu.vo.InvoiceRequestOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/invoice-request")
public class InvoiceRequestController {

    @Autowired
    private InvoiceRequestService invoiceRequestService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order-list")
    public List<InvoiceRequestOrderVO> orderList(@RequestBody InvoiceRequestOrderForm form) throws Exception {
        return orderService.getInvoiceRequestOrderList(form.getStart(), form.getEnd(), form.getCompanyId(), OauthUtils.getCurrentUsername());
    }

    @GetMapping("/list/{comanyId}")
    public IPage<InvoiceRequest> list(@PathVariable Long comanyId,
                               @RequestParam(required = false) String date,
                               @RequestParam(required = false) String start,
                               @RequestParam(required = false) String end,
                               @RequestParam(required = false)String phone,
                               @RequestParam(required = false) Integer status,
                               @RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return invoiceRequestService.getByCompanyId(comanyId, start, end, phone, status, page, size);
    }

    @PutMapping("/submit")
    public void submit(@RequestBody InvoiceRequest request) {
        invoiceRequestService.submit(request);
    }

    @PostMapping("/approved")
    public void approved(@RequestBody InvoiceRequest request) {
        invoiceRequestService.approved(request);
    }

    @PostMapping("/reject")
    public void reject(@RequestBody InvoiceRequest request) {
        invoiceRequestService.reject(request);
    }
}
