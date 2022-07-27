package cn.shinome.usercenter.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户注册请求体
 * @Author chris
 * @Date 2022/4/29, 15:21
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1508881324594323360L;

    /**
     * 用户账户
     */
    String userAccount;

    /**
     * 用户密码
     */
    String userPassword;

    /**
     * 校验密码
     */
    String checkPassword;
}
