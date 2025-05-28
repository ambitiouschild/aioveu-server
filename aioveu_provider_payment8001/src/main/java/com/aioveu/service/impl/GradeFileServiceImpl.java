package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeFileDao;
import com.aioveu.entity.GradeFile;
import com.aioveu.exception.SportException;
import com.aioveu.service.GradeFileService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.SimpleFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeFileServiceImpl extends ServiceImpl<GradeFileDao, GradeFile> implements GradeFileService {

    @Override
    public List<SimpleFileVO> getByGradeId(Long gradeId) {
        QueryWrapper<GradeFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeFile::getGradeId, gradeId)
                .orderByDesc(GradeFile::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            SimpleFileVO simpleFileVO = new SimpleFileVO();
            simpleFileVO.setId(item.getId());
            simpleFileVO.setName(item.getName());
            simpleFileVO.setFilePath(FileUtil.getImageFullUrl(item.getFilePath()));
            return simpleFileVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean upload(Long gradeId, MultipartFile file) throws Exception {
        QueryWrapper<GradeFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeFile::getName, file.getOriginalFilename());
        if (getOne(queryWrapper) != null) {
            throw new SportException(file.getOriginalFilename() + "文件名不能重复！");
        }
        String filePath = "grade/" + file.getOriginalFilename();
        OssUtil.uploadSingleImage(filePath, file.getInputStream());
        GradeFile gradeFile = new GradeFile();
        gradeFile.setGradeId(gradeId);
        gradeFile.setName(file.getOriginalFilename());
        gradeFile.setFilePath(filePath);
        return save(gradeFile);
    }

    @Override
    public boolean deleteById(Long id) {
        GradeFile gradeFile = getById(id);
        if (gradeFile != null) {
            if (OssUtil.deleteFile(gradeFile.getFilePath())) {
                return removeById(id);
            }
        }
        return false;
    }
}
