package cn.codeflyer.trading.controller;

import cn.codeflyer.trading.common.Result;
import cn.codeflyer.trading.entity.User;
import cn.codeflyer.trading.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User userParam) {
        log.info("登录请求 userParam={}", userParam);

        User user = userMapper.selectByName(userParam.getUsername());
        if (user == null) {
            return Result.error("-1", "账号不存在，仅内部账号可登录！");
        }
        if (user.getPassword().equals(userParam.getPassword())) {
            user.setPassword("你猜？");
            return Result.success(user);
        }
        return Result.error("-1", "账号或密码错误！");
    }
}

