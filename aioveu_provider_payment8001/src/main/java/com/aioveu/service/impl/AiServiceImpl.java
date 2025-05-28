package com.aioveu.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.tools.FunctionDefinition;
import com.alibaba.dashscope.tools.ToolCallBase;
import com.alibaba.dashscope.tools.ToolCallFunction;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.AiMessage;
import com.aioveu.entity.ChatHistory;
import com.aioveu.entity.User;
import com.aioveu.exception.SportException;
import com.aioveu.form.ChatForm;
import com.aioveu.service.*;
import com.aioveu.tool.*;
import com.aioveu.utils.AiUtils;
import com.aioveu.utils.DataUtil;
import com.aioveu.utils.SportDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xlfan10
 * @description
 * @date 2025/2/3 18:51
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserWechatIdService userWechatIdService;

    @Autowired
    private UnifiedNoticeService unifiedNoticeService;

    @Autowired
    private GradeAiService gradeAiService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    public static final String DASHSCOPE_API_KEY = "sk-84456cde49c44ab3a5548690cc805b63";

    private ThreadLocal<String> strThreadLocal = new ThreadLocal<>();

    private List<Message> saveChatHistory(String sessionId, Message message) {
        ChatHistory chatHistory = chatHistoryService.getChatHistory(sessionId);
        if (chatHistory != null) {
            List<AiMessage> messages = chatHistory.getMessages();
            if (CollectionUtils.isNotEmpty(messages)) {
                messages.add(message2Ai(message));
                ChatHistory ch = new ChatHistory();
                ch.setMessages(messages);
                ch.setId(sessionId);
                chatHistoryService.updateById(ch);
                return aiMessage2MessageList(messages);
            }
        }
        return null;
    }

    private List<Message> aiMessage2MessageList(List<AiMessage> messages) {
        List<Message> messageList = new ArrayList<>(messages.size());
        for (AiMessage am : messages) {
            Message msg = new Message();
            BeanUtils.copyProperties(am, msg);
            if (am.getToolCalls() != null) {
                msg.setToolCalls(toolCallFunction2Base(am.getToolCalls()));
            }
            messageList.add(msg);
        }
        return messageList;
    }

    private AiMessage message2Ai(Message message) {
        AiMessage aiMessage = new AiMessage();
        BeanUtils.copyProperties(message, aiMessage);
        List<ToolCallBase> toolCalls = message.getToolCalls();
        if (toolCalls != null) {
            aiMessage.setToolCalls(toolCallBase2Function(toolCalls));
        }
        return aiMessage;
    }

    private List<ToolCallFunction> toolCallBase2Function(List<ToolCallBase> toolCalls) {
        List<ToolCallFunction> toolCallFunctions = new ArrayList<>();
        for (ToolCallBase tb : toolCalls) {
            ToolCallFunction tf = (ToolCallFunction) tb;
            toolCallFunctions.add(tf);
        }
        return toolCallFunctions;
    }

    private List<ToolCallBase> toolCallFunction2Base(List<ToolCallFunction> toolCallFunctions) {
        return new ArrayList<>(toolCallFunctions);
    }

    /**
     * 用户第一次使用 未绑定微信号的提示语
     */
    public static String SYSTEM_FIRST_CONTENT = "任务指令：作为线上趣数服务平台的专业客服，为用户%s（用户ID：%s，性别：%s，角色：%s）提供全方位咨询服务，当前时间为%s。在利用内置工具函数处理查询请求时，如信息不足，请主动引导用户提供详细信息。回答策略：1. **主动信息索取**：在需要调用如约课、订单查询等工具功能时，若必要参数（如时间、订单号）缺失，采用友好且明确的语言主动询问：“尊敬的张三，为了快速帮您约课，请说下你要约课的时间好吗？尊敬的张三，为了快速帮您查询订单，请告诉我您的订单号好吗？”2. **精准解答**：基于用户提出的问题，严格参照公司政策与操作流程，结合最新文档内容给予精确解答，避免无关扩展，确保用户问题得到有效解决。3. **透明化操作**：在处理用户请求过程中，如需使用特定工具函数，简要告知用户将采取的步骤，增加服务透明度，例如：“我将通过我们的订单查询系统来获取您的订单详情，请稍候。”4. **确认与跟进**：解答完毕后，确认用户是否满意解答，并主动询问是否有其他可以帮助的地方，如：“张三先生/女士，您的问题已解答完毕，请问还有其他方面需要我的协助吗？”5. **引导用户注册**：对于需要用户注册的场景，引导用户进行注册，主动询问手机号码和验证码进行注册，如：“张三先生/女士，系统检测到您第一次使用还未绑定注册，请告诉我您的手机号码，我发送验证码帮你绑定注册。”注意事项：- 维持专业且亲切的交流风格，确保每一次互动都能提升用户满意度。- 对于所有工具函数调用，务必确保在获取足够且准确的参数后再执行，避免因信息不全导致处理错误。 - 记录重要咨询细节，以便后续跟踪服务或内部评估使用。";

    /**
     * 用户非第一次使用
     */
    public static String SYSTEM_CONTENT = "任务指令：作为线上趣数服务平台的专业客服，为用户%s（用户ID：%s，性别：%s，角色：%s）提供全方位咨询服务，当前时间为%s。在利用内置工具函数处理查询请求时，如信息不足，请主动引导用户提供详细信息。回答策略：1. **主动信息索取**：在需要调用如约课、订单查询等工具功能时，若必要参数（如时间、订单号）缺失，采用友好且明确的语言主动询问：“尊敬的张三，为了快速帮您约课，请说下你要约课的时间好吗？尊敬的张三，为了快速帮您查询订单，请告诉我您的订单号好吗？”2. **精准解答**：基于用户提出的问题，严格参照公司政策与操作流程，结合最新文档内容给予精确解答，避免无关扩展，确保用户问题得到有效解决。3. **透明化操作**：在处理用户请求过程中，如需使用特定工具函数，简要告知用户将采取的步骤，增加服务透明度，例如：“我将通过我们的订单查询系统来获取您的订单详情，请稍候。”4. **确认与跟进**：解答完毕后，确认用户是否满意解答，并主动询问是否有其他可以帮助的地方，如：“张三先生/女士，您的问题已解答完毕，请问还有其他方面需要我的协助吗？”注意事项：- 维持专业且亲切的交流风格，确保每一次互动都能提升用户满意度。- 对于所有工具函数调用，务必确保在获取足够且准确的参数后再执行，避免因信息不全导致处理错误。 - 记录重要咨询细节，以便后续跟踪服务或内部评估使用。";


    @Override
    public String chatAsk(ChatForm chatForm) {
        Message message = chatForm.getMessage();
        if (StringUtils.isEmpty(message.getContent())) {
            throw new SportException("对话内容不能为空");
        }
        if (StringUtils.isEmpty(message.getRole())) {
            message.setRole("user");
        }
        List<Message> messages = saveChatHistory(chatForm.getSessionId(), message);
        if (messages == null) {
            LoginVal currentUser = getCurrentUser(chatForm);
            String nickname = currentUser.getNickname();
            String userId = currentUser.getUserId();
            String gender;
            String role;
            if (currentUser.getGender() == null) {
                gender = "未知";
            } else if (currentUser.getGender() == 1) {
                gender = "男";
            } else if (currentUser.getGender() == 2) {
                gender = "女";
            } else {
                gender = "未知";
            }
            String[] authorities = currentUser.getAuthorities();
            if (authorities == null || authorities.length == 0) {
                role = "普通用户";
            } else {
                Set<String> roleSet = new HashSet<>(Arrays.asList(authorities));
                if (roleSet.contains("teacher")) {
                    role = "教练";
                } else {
                    role = "普通用户";
                }
            }
            List<AiMessage> aiMessageList = new ArrayList<>();
            String time = SportDateUtils.getNowFormatTime();
            String formattedString;
            if (currentUser.isFake()) {
                formattedString = String.format(SYSTEM_FIRST_CONTENT, nickname, userId, gender, role, time);
            } else {
                formattedString = String.format(SYSTEM_CONTENT, nickname, userId, gender, role, time);
            }
            // 首次对话 追加系统角色
            AiMessage system = new AiMessage();
            system.setRole("system");
            system.setContent(formattedString);
            aiMessageList.add(system);
            aiMessageList.add(message2Ai(chatForm.getMessage()));

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setMessages(aiMessageList);
            chatHistory.setUserId(userId);
            chatHistory.setId(chatForm.getSessionId());
            chatHistoryService.save(chatHistory);
            messages = aiMessage2MessageList(aiMessageList);
        } else {
            getCurrentUser(chatForm);
        }
        try {
            return selectTool(messages, chatForm.getSessionId());
        }catch (Exception e) {
            log.error("服务异常:" + e);
            e.printStackTrace();
        }finally {
            strThreadLocal.remove();
        }
        return "系统开小差了";
    }

    private LoginVal getCurrentUser(ChatForm chatForm) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            User user = null;
            if (chatForm.getChatType() == 1) {
                // 公众号 客服消息
                user = userService.getByOpenId(chatForm.getUserId());
            } else if (chatForm.getChatType() == 2) {
                // 微信号
                strThreadLocal.set(chatForm.getUserId());
                String userId = userWechatIdService.getUserIdByWechatId(chatForm.getUserId());
                if (StringUtils.isNotEmpty(userId)) {
                    user = userService.getById(userId);
                }
            } else {
                user = userService.getById(chatForm.getUserId());
            }
            if (user != null) {
                currentUser = DataUtil.getByUser(user, roleUserService.getByUserId(user.getId()));

                OauthUtils.setCurrentUser(currentUser);
            } else {
                currentUser = new LoginVal();
                currentUser.setNickname("");
                currentUser.setUserId(chatForm.getUserId());
                currentUser.setGender(0);
                currentUser.setFake(true);
            }
        }
        return currentUser;
    }


    public String selectTool(List<Message> messages, String sessionId) throws NoApiKeyException, ApiException, InputRequiredException {
        ObjectNode jsonSchemaGrade = AiUtils.generateSchema(GradeTool.class);
        FunctionDefinition fdGrade = FunctionDefinition.builder().name("getGradeNumber")
                .description("当你想通过店铺名称和日期获取店铺在该日期的课程数量时非常有用，返回结果包括日期和课程数量")
                .parameters(JsonUtils.parseString(jsonSchemaGrade.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaOrder = AiUtils.generateSchema(OrderTool.class);
        FunctionDefinition fdOrder = FunctionDefinition.builder().name("getOrderById")
                .description("当你想通过订单id查询订单时非常有用，返回结果包括订单名称和订单日期")
                .parameters(JsonUtils.parseString(jsonSchemaOrder.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaUser = AiUtils.generateSchema(UserTool.class);
        FunctionDefinition fdUser = FunctionDefinition.builder().name("bindUserPhone")
                .description("当检测到用户未绑定注册的时候非常有用，返回结果告诉用户已完成绑定注册")
                .parameters(JsonUtils.parseString(jsonSchemaUser.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaSendCode = AiUtils.generateSchema(SendPhoneCodeTool.class);
        FunctionDefinition fdSendCode = FunctionDefinition.builder().name("sendPhoneCode")
                .description("当需要手机发送验证码的时候非常有用，返回结果告诉用户是否完成验证码发送")
                .parameters(JsonUtils.parseString(jsonSchemaSendCode.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaGradeTemplateCreate = AiUtils.generateSchema(GradeTemplateCreateTool.class);
        FunctionDefinition fdGradeTemplateCreate = FunctionDefinition.builder().name("createFixedGradeTemplate")
                .description("当需要创建临时课程或者指定时间的课程的时候非常有用，需要用户提供班级名称、课券名称、课券消耗课时、限制人数、场馆名称、开始时间和结束时间，时间需要精确到分钟，返回结果告诉用户是否完成创建")
                .parameters(JsonUtils.parseString(jsonSchemaGradeTemplateCreate.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaGradeEnrollCancel = AiUtils.generateSchema(GradeEnrollUserTimeCancelTool.class);
        FunctionDefinition fdGradeEnrollCancel = FunctionDefinition.builder().name("cancelUserTimeGradeEnroll")
                .description("当用户角色是普通用户，需要取消约课的时候非常有用，需要用户提供约课的开始时间和结束时间，时间需要精确到分钟，返回结果告诉用户是否取消成功")
                .parameters(JsonUtils.parseString(jsonSchemaGradeEnrollCancel.toString()).getAsJsonObject()).build();

        ObjectNode jsonSchemaGradeEnrollUserNameTimeCancel = AiUtils.generateSchema(GradeEnrollUserNameTimeCancelTool.class);
        FunctionDefinition fdGradeEnrollUserNameTimeCancel = FunctionDefinition.builder().name("cancelUserNameTimeGradeEnroll")
                .description("当用户角色是普通用户，需要取消约课的时候非常有用，需要用户提供约课的开始时间、结束时间和课程名称，时间需要精确到分钟，返回结果告诉用户是否取消成功")
                .parameters(JsonUtils.parseString(jsonSchemaGradeEnrollUserNameTimeCancel.toString()).getAsJsonObject()).build();

        // 构建生成参数对象，用于配置文本生成的相关参数
        GenerationParam param = GenerationParam.builder()
                // 设置所使用的模型为“qwen-max”
                .model("qwen-max") //qwen-max
                .apiKey(DASHSCOPE_API_KEY)
                // 设置对话历史，用于模型生成时参考
                .messages(messages)
                // 设置生成结果的格式为消息格式
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                // 设置可用的工具函数列表，包括天气查询和时间查询功能
                .tools(Arrays.asList(
                        ToolFunction.builder().function(fdGrade).build(), ToolFunction.builder().function(fdOrder).build(), ToolFunction.builder().function(fdUser).build(),
                        ToolFunction.builder().function(fdSendCode).build(), ToolFunction.builder().function(fdGradeTemplateCreate).build(),
                        ToolFunction.builder().function(fdGradeEnrollCancel).build(), ToolFunction.builder().function(fdGradeEnrollUserNameTimeCancel).build()
                        ))
                .build();

        String finalMsg = loopAsk(messages, sessionId, param);
        while (finalMsg == null) {
            finalMsg = loopAsk(messages, sessionId, param);
        }
        return finalMsg;
    }

    private String loopAsk(List<Message> messages, String sessionId, GenerationParam param) throws NoApiKeyException, InputRequiredException {
        param.setMessages(messages);
        log.info("提问大模型:" + JsonUtils.toJson(messages));
        // 大模型的第一轮调用
        Generation gen = new Generation();
        GenerationResult result = gen.call(param);
        log.info("\n大模型回答信息：" + JsonUtils.toJson(result));
        String answer = resultHandle(result, messages, sessionId);
        if (answer != null) {
            return answer;
        }
        return null;
    }

    private String resultHandle(GenerationResult result, List<Message> messages, String sessionId) {
        for (GenerationOutput.Choice choice : result.getOutput().getChoices()) {
            String finishReason = choice.getFinishReason();
            if ("length".equals(finishReason)) {
                log.error("生成长度导致结束:" + sessionId);
            } else if ("tool_calls".equals(finishReason)) {
                Message toolCallMessage = Message.builder()
                        .role("assistant")
                        .content(result.getOutput().getChoices().get(0).getMessage().getContent())
                        .toolCalls(result.getOutput().getChoices().get(0).getMessage().getToolCalls())
                        .build();
                saveChatHistory(sessionId, toolCallMessage);
            }
            messages.add(choice.getMessage());
            // 如果需要调用工具
            if (result.getOutput().getChoices().get(0).getMessage().getToolCalls() != null) {
                for (ToolCallBase toolCall : result.getOutput().getChoices().get(0).getMessage().getToolCalls()) {
                    if (toolCall.getType().equals("function")) {
                        // 获取工具函数名称和入参
                        String functionName = ((ToolCallFunction) toolCall).getFunction().getName();
                        String functionArgument = ((ToolCallFunction) toolCall).getFunction().getArguments();
                        String functionResponse = functionCallMatch(functionName, functionArgument);
                        log.info("\n工具输出信息：" + functionResponse);
                        Message toolResultMessage = Message.builder()
                                .role("tool")
                                .name(functionName)
                                .content(String.valueOf(functionResponse))
                                .build();
                        saveChatHistory(sessionId, toolResultMessage);
                        messages.add(toolResultMessage);
                    }
                }
            }
            // 如果无需调用工具，直接输出大模型的回复
            else {
                String finalMsg = result.getOutput().getChoices().get(0).getMessage().getContent();
                log.info("\n最终答案：" + finalMsg);
                saveChatHistory(sessionId, Message.builder().role("assistant").content(finalMsg).build());
                return finalMsg;
            }
        }
        return null;
    }

    private String functionCallMatch(String functionName, String functionArgument) {
        log.info("\n工具名称：" + functionName);
        log.info("\n工具参数：" + functionArgument);
        // 大模型判断调用天气查询工具的情况
        if ("getGradeNumber".equals(functionName)) {
            Map<String, Object> params = JsonUtils.fromJson(functionArgument, Map.class);
            if (params.get("date") != null && params.get("storeName") != null) {
                return gradeService.getGradeNumber(params.get("date").toString(), params.get("storeName").toString());
            }
        } else if ("getOrderById".equals(functionName)) {
            OrderTool orderTool = JsonUtils.fromJson(functionArgument, OrderTool.class);
            return orderTool.getOrderById(orderTool.getOrderId());
        } else if ("bindUserPhone".equals(functionName)) {
            Map<String, Object> params = JsonUtils.fromJson(functionArgument, Map.class);
            if (params.get("phone") == null) {
                return "请提供手机号码";
            }
            if (params.get("verificationCode") == null) {
                return "请提供验证码";
            }
            String wxId = strThreadLocal.get();
            log.info("wxId:" + wxId);
            return userWechatIdService.bindWechatId(wxId, params.get("phone").toString(), params.get("verificationCode").toString());
        } else if ("sendPhoneCode".equals(functionName)) {
            Map<String, Object> params = JsonUtils.fromJson(functionArgument, Map.class);
            if (params.get("phone") == null) {
                return "请提供手机号码";
            }
            if (unifiedNoticeService.sendRegisterBindCode(null, null, params.get("phone").toString())) {
                return "验证码已发送，提醒用户提供验证码";
            } else {
                return "系统错误, 请稍后重试!";
            }
        } else if ("createFixedGradeTemplate".equals(functionName)) {
            GradeTemplateCreateTool gradeTemplateCreateTool = JsonUtils.fromJson(functionArgument, GradeTemplateCreateTool.class);
            return gradeAiService.aiCreate(gradeTemplateCreateTool);
        } else if ("cancelUserTimeGradeEnroll".equals(functionName)) {
            GradeEnrollUserTimeCancelTool gradeEnrollUserTimeCancelTool = JsonUtils.fromJson(functionArgument, GradeEnrollUserTimeCancelTool.class);
            return gradeAiService.aiUserTimeCancelEnroll(gradeEnrollUserTimeCancelTool);
        } else if ("cancelUserNameTimeGradeEnroll".equals(functionName)) {
            GradeEnrollUserNameTimeCancelTool gradeEnrollUserNameTimeCancelTool = JsonUtils.fromJson(functionArgument, GradeEnrollUserNameTimeCancelTool.class);
            return gradeAiService.aiUserNameTimeCancelEnroll(gradeEnrollUserNameTimeCancelTool);
        }  else {
            return "函数不存在";
        }
        return "函数入参不全";
    }




}
