package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.form.ProductOrderForm;
import com.aioveu.vo.ProductOrderManagerVO;
import com.aioveu.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/v1/productOrder")
@RestController
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @GetMapping("")
    public IPage<ProductOrderManagerVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String id,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Long categoryId,
                                             @RequestParam(required = false) Integer status) {
        return productOrderService.getManagerAll(page, size, id, name, categoryId, status);
    }

    @GetMapping("/{id}")
    public ProductOrderManagerVO detail(@PathVariable String id) {
        return productOrderService.managerDetail(id);
    }

    @PostMapping("")
    public ProductOrderManagerVO create(@Valid @RequestBody ProductOrderForm form) {
        return productOrderService.createProductOrder(form, OauthUtils.getCurrentUserId());
    }

    @GetMapping("/pay-code/{id}")
    public Map<String, String> getPayCode(@PathVariable String id) {
        return productOrderService.getPayCode(id);
    }

    @GetMapping("/agreement/{id}")
    public Map<String, Object> getAgreementUrl(@PathVariable String id) {
        return productOrderService.getAgreementUrl(id);
    }

    @GetMapping("/agreement-status/{id}")
    public Map<String, Object> getAgreementStatus(@PathVariable String id) {
        return productOrderService.getAgreementStatus(id);
    }

    @PostMapping("/upload-chapter")
    public Map<String, String> uploadChapter(@RequestParam("image") MultipartFile image,
                                @RequestParam("id") String id) {
        return productOrderService.uploadChapter(image, id);
    }

    @GetMapping("/my")
    public List<ProductOrderManagerVO> getMyOrder() {
        return productOrderService.getMyOrder();
    }

    @PostMapping("/sign")
    public Map<String, String> sign(@RequestParam("id") String id) {
        return productOrderService.sign(id);
    }

    @PostMapping("/seal")
    public String createSeal(@RequestParam("name") String name, @RequestParam("id") String id) {
        return productOrderService.createSeal(name, id);
    }

}
