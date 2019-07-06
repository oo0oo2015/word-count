package wang.oo0oo.wordcount.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import wang.oo0oo.wordcount.GlobalConfig;
import wang.oo0oo.wordcount.pojo.User;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping("/")
    public String index(@ModelAttribute User user) {
        return "index";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        User loginUser = checkUser(user) ? user : null;
        if (loginUser == null) {
            //登录失败，返回登录页面
            model.addAttribute("msg", "用户名或密码错误");
            return "index";
        } else {
            //登陆成功
            session.setAttribute("user", loginUser);
            return "main";
        }
    }

    private boolean checkUser(User user) {
        if (user == null || StringUtils.isBlank(user.getUserName()) || StringUtils.isBlank(user.getPassword())) {
            return false;
        }
        for (User i : GlobalConfig.userList) {
            if (i.getUserName().equals(user.getUserName()) && i.getPassword().equals(user.getPassword())) {
                GlobalConfig.loginUser = user;
                return true;
            }
        }
        return false;
    }
}
