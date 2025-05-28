package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeFile;
import com.aioveu.vo.SimpleFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeFileService extends IService<GradeFile> {

    /**
     * 获取班级的课件列表
     * @param gradeId
     * @return
     */
    List<SimpleFileVO> getByGradeId(Long gradeId);

    /**
     * 课件上传
     * @param gradeId
     * @param file
     * @return
     * @throws Exception
     */
    boolean upload(Long gradeId, MultipartFile file) throws Exception;

    /**
     * 根据id删除文件
     * @param id
     * @return
     */
    boolean deleteById(Long id);


}
