package com.aioveu.system.aioveu11Codegen.service;

import com.aioveu.system.aioveu11Codegen.model.query.TablePageQuery;
import com.aioveu.system.aioveu11Codegen.model.vo.CodegenPreviewVO;
import com.aioveu.system.aioveu11Codegen.model.vo.TablePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
  *@ClassName: ConfigService
  *@Description TODO  代码生成配置接口
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/20 18:01
  *@Version 1.0
  **/
public interface CodegenService {

    /**
     * 获取数据表分页列表
     *
     * @param queryParams 查询参数
     * @return
     */
    Page<TablePageVO> getTablePage(TablePageQuery queryParams);

    /**
     * 获取预览生成代码
     *
     * @param tableName 表名
     * @return
     */
    List<CodegenPreviewVO> getCodegenPreviewData(String tableName);

    /**
     * 下载代码
     * @param tableNames 表名
     * @return
     */
    byte[] downloadCode(String[] tableNames);
}
