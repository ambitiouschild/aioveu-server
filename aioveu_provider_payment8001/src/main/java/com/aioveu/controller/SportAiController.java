package com.aioveu.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.tools.FunctionDefinition;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.aioveu.feign.form.WeChatAccountChatForm;
import com.aioveu.form.ChatForm;
import com.aioveu.form.WeChatUserChatForm;
import com.aioveu.service.AiService;
import com.aioveu.service.GradeService;
import com.aioveu.service.impl.AiServiceImpl;
import com.aioveu.tool.AsyncSend;
import com.aioveu.tool.GradeTool;
import com.aioveu.utils.AiUtils;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.aioveu.service.impl.AiServiceImpl.DASHSCOPE_API_KEY;

/**
 * @description
 * @author: luyao
 * @date: 2024/12/22 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
public class SportAiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private GradeService gradeService;

    @PostMapping("/dialog")
    public String dialog(String content){
        GenerationResult result = null;
        try {
            result = callWithMessage(content);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("错误信息："+e.getMessage());
        }
        return null;
    }

    @PostMapping("/ask")
    public String functionCall(@RequestBody ChatForm chatForm) {
        chatForm.setChatType(0);
        return aiService.chatAsk(chatForm);
    }

    @PostMapping("/wechat-user-ask")
    public String weChatUserAsk(@RequestBody WeChatUserChatForm weChatUserChatForm) {
        ChatForm chatForm = new ChatForm();
        chatForm.setUserId(weChatUserChatForm.getUserId());
        chatForm.setSessionId(weChatUserChatForm.getSessionId());
        chatForm.setCompanyId(weChatUserChatForm.getCompanyId());
        Message message = new Message();
        message.setRole(weChatUserChatForm.getRole());
        message.setContent(weChatUserChatForm.getContent());
        chatForm.setMessage(message);
        if (chatForm.getChatType() == null) {
            chatForm.setChatType(2);
        }
        return aiService.chatAsk(chatForm);
    }
    @PostMapping(value = "/function-call-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter functionCallStream(@RequestBody List<Message> messages) throws Exception {
        return selectToolStream(messages);
    }

    @PostMapping("/wechat-ask")
    public Map<String, Object> weChatAsk(@RequestBody WeChatAccountChatForm weChatAccountChatForm) {
        ChatForm chatForm = new ChatForm();
        chatForm.setUserId(weChatAccountChatForm.getUserId());
        chatForm.setSessionId(weChatAccountChatForm.getSessionId());
        Message message = new Message();
        message.setRole(weChatAccountChatForm.getRole());
        message.setContent(weChatAccountChatForm.getContent());
        chatForm.setMessage(message);
        if (chatForm.getChatType() == null) {
            chatForm.setChatType(1);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("answer", aiService.chatAsk(chatForm));
        return data;
    }

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public SseEmitter selectToolStream(List<Message> messages) throws NoApiKeyException, ApiException, InputRequiredException {
        SseEmitter emitter = new SseEmitter();
        ObjectNode jsonSchemaGrade = AiUtils.generateSchema(GradeTool.class);
        FunctionDefinition fdGrade = FunctionDefinition.builder().name("getGradeNumber")
                .description("当你想通过店铺名称和日期获取店铺在该日期的课程数量时非常有用，返回结果包括日期和课程数量")
                .parameters(JsonUtils.parseString(jsonSchemaGrade.toString()).getAsJsonObject()).build();

        // 构建生成参数对象，用于配置文本生成的相关参数
        GenerationParam param = GenerationParam.builder()
                // 设置所使用的模型为“qwen-max”
                .model("qwen-plus") //qwen-max
                .apiKey(DASHSCOPE_API_KEY)
                // 设置对话历史，用于模型生成时参考
                .messages(messages)
                // 设置生成结果的格式为消息格式
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                // 设置可用的工具函数列表，包括天气查询和时间查询功能
                .tools(Arrays.asList(
                        ToolFunction.builder().function(fdGrade).build()))
                .build();
        // 大模型的第一轮调用
        Generation gen = new Generation();
        Flowable<GenerationResult> result = gen.streamCall(param);
        log.info("\n大模型第一轮输出信息：" + JsonUtils.toJson(result));

        executorService.execute(new AsyncSend(result, emitter, gradeService));
        return emitter;
    }

    public GenerationResult callWithMessage(String content) throws Exception{
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(content)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(DASHSCOPE_API_KEY)
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
}
