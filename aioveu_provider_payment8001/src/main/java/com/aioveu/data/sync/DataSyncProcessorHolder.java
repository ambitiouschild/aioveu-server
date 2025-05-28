package com.aioveu.data.sync;

import com.aioveu.data.sync.parent.DataSyncProcessor;
import com.aioveu.exception.SportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description 数据同步处理管理器
 * @author: 雒世松
 * @date: 2025/3/21 15:45
 */
@Component
public class DataSyncProcessorHolder {

    /**
     * 收集系统中所有的 {@link DataSyncProcessor} 接口的实现。
     */
    @Autowired
    private Map<String, DataSyncProcessor> dataSyncProcessorMap;

    /**
     * @param type
     * @return
     */
    public DataSyncProcessor findDataSyncProcessor(String type) {
        String name = type.toLowerCase() + DataSyncProcessor.class.getSimpleName();
        DataSyncProcessor processor = dataSyncProcessorMap.get(name);
        if (processor == null) {
            throw new SportException("数据同步处理器" + name + "不存在");
        }
        return processor;
    }




}
