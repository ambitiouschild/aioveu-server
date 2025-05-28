package com.aioveu.service.impl;

import cn.hutool.core.net.url.UrlBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.CheckRecordDao;
import com.aioveu.entity.*;
import com.aioveu.enums.CheckRecordStatus;
import com.aioveu.enums.FieldPlanLockChannels;
import com.aioveu.exception.SportException;
import com.aioveu.qrcode.QrCodeDeWrapper;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import com.aioveu.utils.PositionUtil;
import com.aioveu.utils.RedisUtil;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CheckRecordServiceImpl extends ServiceImpl<CheckRecordDao, CheckRecord> implements CheckRecordService {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckRecordAccountConfigService checkRecordAccountConfigService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisUtil redisTemplate;

    @Override
    public boolean create(CheckRecord checkRecord, String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new SportException(username + "不存在");
        }
        checkRecord.setCreateUser(user.getUsername());
        checkRecord.setCreateUserId(user.getId());
        return save(checkRecord);
    }

    @Override
    public IPage<CheckRecord> getList(int page, int size, String username, Long storeId) {
        QueryWrapper<CheckRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CheckRecord::getStoreId, storeId);
        UserVo user = userService.findByUsername(username);
        List<String> roles = user.getRoles();
        if (roles.contains("brush_user")) {

        } else {
            queryWrapper.lambda().eq(CheckRecord::getCreateUserId, user.getId());
        }
        queryWrapper.lambda().orderByDesc(CheckRecord::getCreateDate);
        Page<CheckRecord> checkRecordPage = page(new Page<>(page, size), queryWrapper);
        List<CheckRecord> records = checkRecordPage.getRecords();
        records.forEach(record -> {
            record.setImage(FileUtil.getImageFullUrl(record.getImage()));
        });
        return checkRecordPage;
    }

    @Override
    public String check(Long id, String username) {
        UserVo user = userService.findByUsername(username);
        if (user == null) {
            throw new SportException(username + "不存在");
        }
        List<String> roles = user.getRoles();
        if (roles.contains("brush_user") || roles.stream().anyMatch(s -> s.startsWith("admin"))) {
            Object value = redisTemplate.get("check_order_" + OauthUtils.getCurrentUserId());
            if (value != null){
                throw new SportException("请勿频繁操作，请稍后再操作");
            }
            CheckRecord record = getById(id);
            if (record == null || record.getStatus() != CheckRecordStatus.Uncheck.getCode()){
                throw new SportException("请勿重复操作");
            }
            CheckRecordAccountConfig accountConfig = checkRecordAccountConfigService.getOneByStoreId(record.getStoreId(), Long.valueOf(FieldPlanLockChannels.LHD.getCode()));
            if (accountConfig == null){
                throw new SportException("未配置账号");
            }
            SyncDataAccountConfig config = new SyncDataAccountConfig();
            BeanUtils.copyProperties(accountConfig,config);
            Integer status = CheckRecordStatus.Checked.getCode();
            CheckRecord checkRecord = new CheckRecord();
            checkRecord.setStatus(status);
            checkRecord.setId(id);
            updateById(checkRecord);
            return CheckRecordStatus.of(status).getDescription();
        } else {
            throw new SportException(username + "没有权限");
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids, String username) {
        List<CheckRecord> checkRecords = listByIds(ids);
        for (CheckRecord checkRecord : checkRecords) {
            OssUtil.deleteFile(checkRecord.getImage());
        }
        return removeByIds(ids);
    }

    @Override
    public String uploadPreCheck(Long storeId, Long companyId, String prefix, MultipartFile file, String scanCode) {
        Integer scanErrorCode = 2002;
        String decode = null;
        try {
            decode = QrCodeDeWrapper.decode(ImageIO.read(file.getInputStream()));
        } catch (Exception e) {
            decode = scanCode;
        }
        if (StringUtils.isBlank(decode)) {
            throw new SportException(scanErrorCode,"核销码识别失败");
        }
        Double la = null;
        Double lo = null;
        try {
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(decode);
            la = Double.valueOf(urlBuilder.getQuery().get("latitude").toString());
            lo = Double.valueOf(urlBuilder.getQuery().get("longitude").toString());
        } catch (Exception e){
            log.error("解析失败:" + e.getMessage());
            throw new SportException("核销码不规范");
        }
        Store store = storeService.getById(storeId);
        Double distance = PositionUtil.getDistance4(store.getLongitude(), store.getLatitude(), lo, la);
        if (distance > 500){
            log.error("距离太远:{}" + distance);
            throw new SportException("距离太远，上传失败");
        }
        try {
            String upload = uploadService.upload(prefix, file);
            CheckRecord checkRecord = new CheckRecord();
            checkRecord.setImage(upload);
            checkRecord.setCodeText(decode);
            checkRecord.setStoreId(storeId);
            checkRecord.setCompanyId(companyId);
            checkRecord.setCreateUser(OauthUtils.getCurrentUsername());
            checkRecord.setCreateUserId(OauthUtils.getCurrentUserId());
            save(checkRecord);
            return upload;
        } catch (Exception e) {
            throw new SportException("文件上传失败");
        }
    }

}
