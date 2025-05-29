package com.aioveu.auth.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@TableName("sport_role_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {

    @Id
    private Long id;

    private String userId;

    private String roleCode;

    private Integer status;

}
