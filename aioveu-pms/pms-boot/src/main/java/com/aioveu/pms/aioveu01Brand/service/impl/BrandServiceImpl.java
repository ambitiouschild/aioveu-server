package com.aioveu.pms.aioveu01Brand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu01Brand.mapper.PmsBrandMapper;
import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;
import com.aioveu.pms.aioveu01Brand.service.BrandService;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements BrandService {
}
