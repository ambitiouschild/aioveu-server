package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreImageDao;
import com.aioveu.entity.StoreImage;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.StoreImageService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.StoreImageDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class StoreImageServiceImpl extends ServiceImpl<StoreImageDao, StoreImage> implements StoreImageService {

    @Autowired
    private StoreImageDao storeImageDao;

    @Override
    public boolean deleteImage(Long id) {
        StoreImage storeImage = getById(id);
        if (storeImage == null) {
            return false;
        }
        OssUtil.deleteFile(storeImage.getUrl());
        return removeById(id);
    }

    @Override
    public List<StoreImage> getByStoreId(Long storeId) {
        QueryWrapper<StoreImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreImage::getStoreId, storeId)
                .eq(StoreImage::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(StoreImage::getPriority);
        return list(queryWrapper).stream().peek(item -> item.setUrl(FileUtil.getImageFullUrl(item.getUrl()))).collect(Collectors.toList());
    }

    @Override
    public StoreImageDetailVO managerDetail(Long id) {
        StoreImage storeImage = getById(id);
        if (storeImage != null ){
            StoreImageDetailVO storeImageDetaiVO = new StoreImageDetailVO();
            BeanUtils.copyProperties(storeImage,storeImageDetaiVO);
            return storeImageDetaiVO;
        }
        return null;
    }


    @Override
    public IPage<StoreImageDetailVO> getManagerAll(int page, int size, Long storeId) {
        return storeImageDao.getManagerAll(new Page<>(page, size),storeId);
    }


}
