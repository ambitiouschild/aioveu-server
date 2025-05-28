package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.UserCall;
import com.aioveu.enums.IntentionStatus;
import com.aioveu.enums.UserCallStatus;
import com.aioveu.form.UserCallFollow;
import com.aioveu.form.UserCallForm;
import com.aioveu.form.UserCallUpdateForm;
import com.aioveu.service.UserCallService;
import com.aioveu.service.UserReceiveCallService;
import com.aioveu.service.UserTagService;
import com.aioveu.vo.NewUserInfoVO;
import com.aioveu.vo.UserCallSimpleVO;
import com.aioveu.vo.UserCallVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user-call")
public class UserCallController {

    @Autowired
    private UserCallService userCallService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserReceiveCallService userReceiveCallService;

    @GetMapping("")
    public IPage<UserCallVO> list(@RequestParam (required = false) Integer page,
                                  @RequestParam (required = false) Integer size,
                                  @RequestParam String userId,
                                  @RequestParam Integer type,
                                  @RequestParam Long storeId,
                                  @RequestParam(required = false) String keyword) {
        return userCallService.getList(page, size, userId, keyword, type, storeId);
    }

    @GetMapping("/getAllByPage")
    public IPage<UserCallVO> getAllByPage(@RequestParam (required = false) Integer page,
                                  @RequestParam (required = false) Integer size,
                                  @RequestParam Integer type,
                                  @RequestParam Long storeId,
                                  @RequestParam(required = false) String keyword) {
        return userCallService.getAllListByPage(page, size, storeId, keyword, type);
    }

    @GetMapping("/new-list")
    public IPage<NewUserInfoVO> newUserInfoList(@RequestParam (required = false) Integer page,
                                                @RequestParam (required = false) Integer size,
                                                @RequestParam(required = false) String createUserId,
                                                @RequestParam(required = false) String userId,
                                                @RequestParam(required = false) String timeFrom,
                                                @RequestParam(required = false) String timeTo,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(required = false) String keyword) {
        return userCallService.newUserInfoList(page, size, createUserId, userId, timeFrom, timeTo, status, keyword);
    }

    @GetMapping("/simple")
    public IPage<UserCallSimpleVO> simpleList(@RequestParam (required = false) Integer page,
                                        @RequestParam (required = false) Integer size,
                                        @RequestParam Long userInfoId) {
        return userCallService.getSimpleList(page, size, userInfoId);
    }

    @PostMapping("")
    public boolean add(@Valid @RequestBody UserCallForm form) {
        return userCallService.batchAdd(form.getUserId(), form.getUserInfoList(), form.getType());
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody UserCallUpdateForm form) {
        return userCallService.updateUserCall(form);
    }

    @PostMapping("/follow")
    public boolean callFollow(@Valid @RequestBody UserCallFollow form) {
        return userCallService.userCallFollow(form);
    }

    @GetMapping("/{id}")
    public UserCall get(@PathVariable Long id) {
        UserCall userCall = userCallService.getById(id);
        if (userCall != null) {
            userCall.setCallStatusName(UserCallStatus.of(userCall.getCallStatus()).getDescription());
            userCall.setIntentionName(IntentionStatus.of(userCall.getIntention()).getDescription());
            userCall.setTagList(userTagService.getByUserInfoId(userCall.getUserInfoId()));
        }
        return userCall;
    }

    @GetMapping("/tag")
    public List<String> tagList() {
        return Arrays.asList("价格敏感", "暂无需求", "过段时间联系", "注重教练");
    }

    @DeleteMapping("")
    public boolean deleteByIds(@RequestBody List<Long> idList) {
        return userReceiveCallService.deleteByIds(idList, OauthUtils.getCurrentUserId(), OauthUtils.getCurrentNickname());
    }

    @PostMapping("/updateUserReceiveCalls")
    public boolean updateUserReceiveCalls(@RequestParam String userId,
                                          @RequestParam(required = false) String coachUserId,
                                          @RequestParam(required = false) String salesUserId,
                                          @RequestParam String companyId,
                                          @RequestParam String phone) {
        return userCallService.updateUserReceiveCalls(userId,phone,coachUserId,salesUserId,companyId);
    }

    @PostMapping("/updateUserReceiveCallToUser")
    public boolean updateUserReceiveCallToUser(@RequestParam String userId,
                                          @RequestParam String transferPhone) {
        return userCallService.updateUserReceiveCallToUser(userId, transferPhone);
    }
}
