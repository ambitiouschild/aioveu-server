package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreVenueDao;
import com.aioveu.entity.IdNameEntity;
import com.aioveu.entity.StoreVenue;
import com.aioveu.entity.VenueField;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.StoreVenueService;
import com.aioveu.service.VenueFieldService;
import com.aioveu.utils.FileUtil;
import com.aioveu.vo.StoreVenueItemVO;
import com.aioveu.vo.StoreVenueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Service
public class StoreVenueServiceImpl extends ServiceImpl<StoreVenueDao, StoreVenue> implements StoreVenueService {

    @Autowired
    private VenueFieldService venueFieldService;

    @Override
    public IPage<StoreVenueItemVO> getByStoreId(int page, int size, Integer status, Long storeId) {
        IPage<StoreVenueItemVO> iPage = getBaseMapper().getByStoreId(new Page<>(page, size), status, storeId);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            iPage.setRecords(iPage.getRecords()
                    .stream()
                    .filter(item -> item.getId() != null)
                    .peek(item -> {
                        item.setLogo(FileUtil.getImageFullUrl(item.getLogo()));
                        item.setVenueFieldList(this.venueFieldService.findByVenueId(item.getId()));
                    })
                    .collect(Collectors.toList()));
        }
        return iPage;
    }

    @Override
    public List<StoreVenue> findByStoreId(Long storeId) {
        QueryWrapper<StoreVenue> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreVenue::getStoreId, storeId).eq(StoreVenue::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public StoreVenueVO getDetail(Long venueId) {
        StoreVenue storeVenue = this.getById(venueId);
        StoreVenueVO storeVenueVO = new StoreVenueVO();
        BeanUtils.copyProperties(storeVenue, storeVenueVO);
        List<VenueField> venueFieldList = this.venueFieldService.findByVenueId(storeVenue.getId());
        storeVenueVO.setVenueFieldList(venueFieldList);
        return storeVenueVO;
    }

    @Override
    public void saveStoreVenue(StoreVenueVO storeVenueVO) {
        StoreVenue storeVenue = new StoreVenue();
        BeanUtils.copyProperties(storeVenueVO, storeVenue);
        if (storeVenueVO.getBookOpen() == null) {
            storeVenue.setBookOpen(true);
        }
        if (storeVenue.getId() != null) {
            this.updateById(storeVenue);
        } else {
            this.save(storeVenue);
        }
        Map<Long, VenueField> venueFieldMap = this.venueFieldService.findByVenueId(storeVenue.getId()).stream().collect(Collectors.toMap(IdNameEntity::getId, t -> t));
        if (storeVenueVO.getVenueFieldList() != null) {
            for (VenueField venueField : storeVenueVO.getVenueFieldList()) {
                if (venueFieldMap.containsKey(venueField.getId())) {
                    this.venueFieldService.updateById(venueField);
                    venueFieldMap.remove(venueField.getId());
                } else {
                    venueField.setStoreId(storeVenue.getStoreId());
                    venueField.setVenueId(storeVenue.getId());
                    this.venueFieldService.save(venueField);
                }
            }
        }
        for (VenueField value : venueFieldMap.values()) {
            value.setStatus(DataStatus.DELETE.getCode());
            this.venueFieldService.updateById(value);
        }
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        StoreVenue gt = new StoreVenue();
        gt.setId(id);
        gt.setStatus(status);
        return updateById(gt);
    }

    @Override
    public List<StoreVenue> getByName(String name, Long storeId) {
        QueryWrapper<StoreVenue> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StoreVenue::getName, name).eq(StoreVenue::getStoreId, storeId);
        return list(queryWrapper);
    }
}
