package com.aioveu.tenant.aioveu11Codegen.mapper;

import com.aioveu.tenant.aioveu11Codegen.model.bo.ColumnMetaData;
import com.aioveu.tenant.aioveu11Codegen.model.bo.TableMetaData;
import com.aioveu.tenant.aioveu11Codegen.model.query.TableQuery;
import com.aioveu.tenant.aioveu11Codegen.model.vo.TablePageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: DatabaseMapper
 * @Description TODO 数据库映射层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:59
 * @Version 1.0
 **/
@Mapper
public interface DatabaseMapper extends BaseMapper {

    /**
     * 获取表分页列表
     *
     * @param page
     * @param queryParams
     * @return
     */
    Page<TablePageVO> getTablePage(Page<TablePageVO> page, TableQuery queryParams);

    /**
     * 获取表字段列表
     *
     * @param tableName
     * @return
     */
    List<ColumnMetaData> getTableColumns(String tableName);

    /**
     * 获取表元数据
     *
     * @param tableName
     * @return
     */
    TableMetaData getTableMetadata(String tableName);
}
