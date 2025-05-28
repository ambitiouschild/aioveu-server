package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Agreement;
import com.aioveu.vo.AgreementDetailVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface AgreementDao extends BaseMapper<Agreement> {

    /**
     * 列表
     * @param page
     * @param companyId
     * @return
     */
    IPage<AgreementDetailVO> getManagerAll(IPage<AgreementDetailVO> page, Long companyId);

}
