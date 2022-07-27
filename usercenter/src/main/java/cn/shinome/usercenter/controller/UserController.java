package cn.shinome.usercenter.controller;

import cn.shinome.usercenter.entity.User;
import cn.shinome.usercenter.dto.UserLoginRequest;
import cn.shinome.usercenter.dto.UserRegisterRequest;
import cn.shinome.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static cn.shinome.usercenter.constant.UserConstant.ADMIN_ROLE;
import static cn.shinome.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @Description 用户接口
 * @Author chris
 * @Date 2022/4/29, 15:11
 */
@RestController // 适用于编写 restful 风格的 api，返回值摸认为 json 类型。
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // Controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身 (越少越好)。
        // Service 层是对业务逻辑的校验，可能会被 controller 之外的类调用。
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // Controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身 (越少越好)。
        // Service 层是对业务逻辑的校验，可能会被 controller 之外的类调用。
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        User currUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currUser == null) {
            return null;
        }
        // 如果不变的话可以直接返回用户信息，如果频繁变化的话需要查表
        long userId = currUser.getId();
        // todo 校验用户登录是否合法
        User user = userService.getById(userId);
        return userService.getHandledUser(user);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        return userService.searchUsers(username, request);
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 鉴权
     * @param request 请求
     * @return 是否是管理员
     * @Author chris
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
