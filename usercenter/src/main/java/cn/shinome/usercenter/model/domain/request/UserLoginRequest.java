package cn.shinome.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户登陆请求体
 * @Author chris
 * @Date 2022/4/29, 15:46
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1508881324594323360L;

    /**
     * 用户账户
     */
    String userAccount;

    /**
     * 用户密码
     */
    String userPassword;

}
