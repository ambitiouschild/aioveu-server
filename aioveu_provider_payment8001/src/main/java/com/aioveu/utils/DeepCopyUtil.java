package com.aioveu.utils;

import com.alibaba.dashscope.common.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeepCopyUtil {
    public static <T extends Serializable> List<T> deepCopy(List<T> original) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(original);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (List<T>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deep copy list", e);
        }
    }

    public static List<Message> copyList(List<Message> original) {
        List<Message> copyList = new ArrayList<>();
        for (Message message : original) {
            Message msg = new Message();
            msg.setRole(message.getRole());
            msg.setContent(message.getContent());
            msg.setName(message.getName());
            msg.setToolCallId(message.getToolCallId());
            msg.setToolCalls(message.getToolCalls());
            msg.setContents(message.getContents());
            copyList.add(msg);
        }
        return copyList;
    }

}