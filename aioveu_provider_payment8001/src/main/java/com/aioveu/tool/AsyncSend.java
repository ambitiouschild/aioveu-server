package com.aioveu.tool;

import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.tools.ToolCallBase;
import com.alibaba.dashscope.tools.ToolCallFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.aioveu.service.GradeService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AsyncSend implements Runnable {

    private Flowable<GenerationResult> result;

    private SseEmitter emitter;

    private GradeService gradeService;

    public AsyncSend(Flowable<GenerationResult> result, SseEmitter emitter, GradeService gradeService) {
        this.result = result;
        this.emitter = emitter;
        this.gradeService = gradeService;
        this.messages = new ArrayList<>();
    }

    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void run() {
        result.blockingForEach(msg -> {
            for (GenerationOutput.Choice choice : msg.getOutput().getChoices()) {
                messages.add(choice.getMessage());
                // 如果需要调用工具
                if (msg.getOutput().getChoices().get(0).getMessage().getToolCalls() != null) {
                    for (ToolCallBase toolCall : msg.getOutput().getChoices().get(0).getMessage().getToolCalls()) {
                        if (toolCall.getType().equals("function")) {
                            // 获取工具函数名称和入参
                            String functionName = ((ToolCallFunction) toolCall).getFunction().getName();
                            String functionArgument = ((ToolCallFunction) toolCall).getFunction().getArguments();
                            // 大模型判断调用天气查询工具的情况
                            if (functionName.equals("getGradeNumber")) {
                                log.info("\n工具名称：" + functionName);
                                GradeTool gradeTool = JsonUtils.fromJson(functionArgument, GradeTool.class);
                                log.info("\n工具参数：" + functionArgument);
                                String gradeNumber = gradeService.getGradeNumber(gradeTool.getDate(), gradeTool.getStoreName());
                                Message toolResultMessage = Message.builder()
                                        .role("tool")
                                        .content(String.valueOf(gradeNumber))
                                        .toolCallId(toolCall.getId())
                                        .build();
                                messages.add(toolResultMessage);
                                System.out.println("\n工具输出信息：" + gradeNumber);
                            }
                        }
                    }
                }
                // 如果无需调用工具，直接输出大模型的回复
                else {
                    // 模拟数据生成
                    emitter.send(msg.getOutput().getChoices().get(0).getMessage().getContent());
                }
            }
        });
        emitter.complete();
    }




}
