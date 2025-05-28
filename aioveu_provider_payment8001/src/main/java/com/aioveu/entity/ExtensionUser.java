package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sport_extension_user")
public class ExtensionUser extends IdEntity {
    private String name;
    private String extensionId;
    private String userId;
}
