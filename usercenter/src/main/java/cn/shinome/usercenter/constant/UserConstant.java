package cn.shinome.usercenter.constant;

/**
 * @Description 用户常量
 * @Author chris
 * @Date 2022/4/29, 17:11
 */
public interface UserConstant {

    /**
     * 默认头像
     */
    String DEFAULT_AVATAR = "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/7246/defaultAvatar.jpg";

    /**
     * 用户登陆态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    // 权限
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

}
