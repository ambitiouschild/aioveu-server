package com.aioveu.form;

import com.aioveu.vo.user.BaseUser;
import lombok.Data;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/10/28 23:50
 * @Describe：
 */
@Data
public class CompanyStoreUserForm extends BaseUser {

    private String userId;

    private List<Long> storeIdList;

    private String creatorId;

    private Long storeId;

    private Long companyId;
}
