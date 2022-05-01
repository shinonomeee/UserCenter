package cn.shinome.usercenter.service.impl;

import cn.shinome.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.shinome.usercenter.model.domain.User;
import cn.shinome.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.shinome.usercenter.constant.GlobalSecretConstants.SALT;
import static cn.shinome.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author chris
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-04-26 15:48:54
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // todo 修改为自定义异常
            return -1;
        }
        // 校验账户
        // 账户名不小于 4 位
        if (userAccount.length() < 4) {
            return -1;
        }
        // 密码不小于 8 位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        // 校验账户包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        int count = this.count(userQueryWrapper);
        if (count > 0) {
            return -1;
        }
        // 2. 加密
        String qualifiedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 3. 插入数据
        User newUser = new User();
        newUser.setUserAccount(userAccount);
        newUser.setUserPassword(qualifiedPassword);
        boolean saveResult = this.save(newUser);
        if (!saveResult) {
            return -1;
        }
        return newUser.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 校验账户
        // 账户名不小于 4 位
        if (userAccount.length() < 4) {
            return null;
        }
        // 密码不小于 8 位
        if (userPassword.length() < 8) {
            return null;
        }
        // 校验账户包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 2. 加密
        String qualifiedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        userQueryWrapper.eq("user_password", qualifiedPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 3. 用户脱敏
        User handledUser = getHandledUser(user);
        // 4. 记录用户的登陆态 (tomcat)
        /*
         * 如何知道是哪个用户登陆了?
         * 1 连接服务器后，会得到一个 session1 状态 (匿名会话)，返回给前端。
         * 2 登陆成功之后，得到了登陆成功的 session 并给该 session 设置一些值 (比如用户信息)，
         *   返回给前端一个设置 cookie 的"命令"。
         *   session => cookie
         * 3 前端接收到后端的命令后，设置 cookie，并找到对应的session。
         * 4 前端再次请求后端的时候 (要求相同的域名)，在请求头中带上 cookie 来请求。
         * 5 后端拿到前端传来的 cookie，找到对应的 session。
         * 6 后端从session中可以取出基于改 session 储存的变量 (用户的登陆信息、登陆名等)。
         */
        request.getSession().setAttribute(USER_LOGIN_STATE, handledUser);
        return handledUser;
    }

    @Override
    public User getHandledUser(User originalUser) {
        User handledUser = new User();
        handledUser.setId(originalUser.getId());
        handledUser.setUsername(originalUser.getUsername());
        handledUser.setUserAccount(originalUser.getUserAccount());
        handledUser.setAvatarUrl(originalUser.getAvatarUrl());
        handledUser.setGender(originalUser.getGender());
        handledUser.setPhone(originalUser.getPhone());
        handledUser.setEmail(originalUser.getEmail());
        handledUser.setUserStatus(originalUser.getUserStatus());
        handledUser.setUserRole(originalUser.getUserRole());
        handledUser.setCreateTime(originalUser.getCreateTime());
        return handledUser;
    }

    @Override
    public List<User> searchUsers(String username, HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getHandledUser).collect(Collectors.toList());
    }
}




