package com.aioveu.service.impl;

import com.aioveu.entity.UserViewPosition;
import com.aioveu.form.LocationRecordForm;
import com.aioveu.service.CategoryService;
import com.aioveu.service.LocationService;
import com.aioveu.service.UserViewPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xlfan10
 * @description
 * @date 2025/3/4 12:51
 */
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private UserViewPositionService userViewPositionService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean record(LocationRecordForm form) {
        UserViewPosition userViewPosition = new UserViewPosition();
        userViewPosition.setLatitude(form.getLatitude());
        userViewPosition.setLongitude(form.getLongitude());
     // userViewPosition.setAddress(resultEntity.getFormatted_address());
     // userViewPosition.setBusiness(resultEntity.getBusiness());
     // userViewPosition.setLatitude(locationEntity.getLat());
     // userViewPosition.setLongitude(locationEntity.getLng());
     // userViewPosition.setProvince(addressComponentEntity.getProvince());
     // userViewPosition.setCity(addressComponentEntity.getCity());
     // userViewPosition.setDistrict(addressComponentEntity.getDistrict());
     // userViewPosition.setTown(addressComponentEntity.getTown());
     // userViewPosition.setStreet(addressComponentEntity.getStreet());
     // userViewPosition.setAdcode(addressComponentEntity.getAdcode());
        userViewPosition.setUserId(form.getUserId());
        if (form.getCategoryId() == null) {
            userViewPosition.setCategoryId(categoryService.getByCode(form.getCategoryCode()));
        } else {
            userViewPosition.setCategoryId(form.getCategoryId());
        }
        userViewPosition.setProductId(form.getProductId());
        return userViewPositionService.save(userViewPosition);
    }
}
