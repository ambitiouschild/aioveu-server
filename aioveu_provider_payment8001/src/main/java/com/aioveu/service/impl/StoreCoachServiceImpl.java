package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreCoachDao;
import com.aioveu.entity.CoachCertificate;
import com.aioveu.entity.Store;
import com.aioveu.entity.StoreCoach;
import com.aioveu.service.CoachCertificateService;
import com.aioveu.service.CoachTagService;
import com.aioveu.service.StoreCoachService;
import com.aioveu.service.StoreService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.StoreCoachUserVO;
import com.aioveu.vo.StoreCoachVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class StoreCoachServiceImpl extends ServiceImpl<StoreCoachDao, StoreCoach> implements StoreCoachService {

    @Autowired
    private CoachTagService coachTagService;

    @Autowired
    @Resource
    private CoachCertificateService coachCertificateService;

    @Autowired
    private StoreService storeService;

    @Override
    public IPage<StoreCoachVO> getByStoreId(Long storeId, boolean hasBindUser, Integer userType, int page, int size) {
        IPage<StoreCoachVO> pageResult = getBaseMapper().getByStoreId(new Page<>(page, size), storeId, hasBindUser, userType);
        pageResult.setRecords(pageResult.getRecords().stream()
                .peek(item -> {
                    item.setUrl(FileUtil.getImageFullUrl(item.getUrl()));
                    item.getTagList().forEach(tag -> {
                        tag.setIcon(FileUtil.getImageFullUrl(tag.getIcon()));
                    });
                })
                .collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public List<StoreCoachVO> getListByStoreId(Long storeId, Integer userType) {
        List<StoreCoachVO> byStoreId = getBaseMapper().getByStoreId(storeId, userType, true);
        return byStoreId;
    }

    @Override
    public boolean deleteCoachById(Long id) {
        StoreCoach storeCoach = getById(id);
        if (storeCoach == null) {
            return false;
        }
        storeCoach.setStatus(0);
        if (updateById(storeCoach)) {
            OssUtil.deleteFile(storeCoach.getUrl());
            return true;
        }
        return false;
    }

    @Override
    public Boolean createOrUpdate(StoreCoach storeCoach) {
        try {
            if (storeCoach.getCompanyId() == null) {
                Store store = storeService.getById(storeCoach.getStoreId());
                storeCoach.setCompanyId(store.getCompanyId());
            }
            if (storeCoach.getId() != null) {
                // 更新
                StoreCoach sc = getById(storeCoach.getId());
                if (!FileUtil.getImageFullUrl(sc.getUrl()).equals(storeCoach.getUrl())) {
                    OssUtil.deleteFile(sc.getUrl());
                }
            }
            boolean result = saveOrUpdate(storeCoach);
            String expertise = storeCoach.getExpertise();
            if (StringUtils.isNotEmpty(expertise)) {
                coachTagService.createOrUpdateTag(expertise, storeCoach.getId());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StoreCoach detail(Long id) {
        StoreCoach storeCoach = getById(id);
        if (storeCoach != null) {
            storeCoach.setUrl(FileUtil.getImageFullUrl(storeCoach.getUrl()));
            // 查询教练证书
            List<CoachCertificate> byCoachId = coachCertificateService.getByCoachId(id);
            storeCoach.setCertificate(byCoachId);
        }
        return storeCoach;
    }

    @Override
    public List<StoreCoachUserVO> getCreateUserCoachList(Integer userType, String storeId, String companyId) {
        return getBaseMapper().getCreateUserCoachList(userType,storeId,companyId);
    }

    @Override
    public List<StoreCoach> getByUserAndStoreId(String userId, Long storeId) {
        return getBaseMapper().getByUserAndStoreId(userId, storeId);
    }

    @Override
    public List<StoreCoachVO> getStoreCoachUser(Long storeId) {
        return getBaseMapper().getStoreCoachUser(storeId);
    }

    @Override
    public StoreCoach getByUserIdAndStoreId(String userId, Long storeId, Integer userType) {
        List<StoreCoach> storeCoachList = getByUserAndStoreId(userId, storeId);
        if (CollectionUtils.isNotEmpty(storeCoachList)) {
            return storeCoachList.stream().filter(item -> item.getUserType().equals(userType)).findFirst().orElse(null);
        }
        return null;
    }
}
