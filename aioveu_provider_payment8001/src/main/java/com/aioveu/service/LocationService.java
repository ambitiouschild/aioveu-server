package com.aioveu.service;

import com.aioveu.form.LocationRecordForm;

/**
 * @author xlfan10
 * @description
 * @date 2025/3/4 12:49
 */
public interface LocationService {

    /**
     * 位置记录
     * @param form
     * @return
     */
    boolean record(LocationRecordForm form);

}
