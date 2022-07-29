package cn.shinome.usercenter.service;

import cn.shinome.usercenter.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chris
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2022-04-26 15:48:54
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     * @Author chris
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏后的用户信息
     * @Author chris
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销接口
     *
     * @param request 请求
     * @return 注销成功/失败标识
     * @Author chris
     */
    int userLogout(HttpServletRequest request);

    /**
     * 为用户信息脱敏
     * @param originalUser 未脱敏的 User
     * @return 脱敏后的 User
     * @Author chris
     */
    User getHandledUser(User originalUser);

    /**
     * 模糊搜索用户 (仅限管理员)
     * @param username 搜索的用户名
     * @param request 请求头
     * @return 搜索结果
     * @Author chris
     */
    List<User> searchUsers(String username, HttpServletRequest request);


}
