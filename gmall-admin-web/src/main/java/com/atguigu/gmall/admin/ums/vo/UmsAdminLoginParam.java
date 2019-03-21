package com.atguigu.gmall.admin.ums.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * 用户登录参数
 * Created by atguigu 4/26.
 */
@Data
public class UmsAdminLoginParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

}
