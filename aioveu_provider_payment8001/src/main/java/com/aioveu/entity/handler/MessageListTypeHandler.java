package com.aioveu.entity.handler;

import com.alibaba.fastjson.TypeReference;
import com.aioveu.entity.AiMessage;

import java.util.List;

public class MessageListTypeHandler extends ListTypeHandler<AiMessage> {
    @Override
    protected TypeReference<List<AiMessage>> specificType() {
        return new TypeReference<List<AiMessage>>() { };
    }
}
