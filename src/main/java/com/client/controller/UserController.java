package com.client.controller;

import com.client.model.User;
import com.client.model.UserRole;
import com.client.service.UserRoleService;
import com.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String showUserList(Model model, HttpServletRequest req) {
        model.addAttribute("users", userService.getAllUsers());
        User user = new User();
        model.addAttribute("editUser", user);
        List<UserRole> userRoles = userRoleService.getAllUserRoles();
        model.addAttribute("userRoles", userRoles);
        return "admin";
    }

    @PostMapping(value = "/admin")
    public String saveUser(@RequestBody User user) {
        final String EMPTY__PASSWORD_VALUE = "";
        String encryptedPassword;

        if (user.getPassword().equals(EMPTY__PASSWORD_VALUE)) {
            User currentUserData = userService.getUserById(user.getId());
            encryptedPassword = currentUserData.getPassword();
        } else {
            encryptedPassword = passwordEncoder.encode(user.getPassword());
        }
        user.setPassword(encryptedPassword);

        if (user.getId() == 0) {
            userService.createUser(user);
        } else {
            userService.updateUser(user);
        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String showUserPage() {
        return "user";
    }

    @GetMapping("/admin/user/remove")
    public String removeUser(HttpServletRequest req) {
        int userIdToRemove = Integer.parseInt(req.getParameter("id"));
        userService.removeUser(userIdToRemove);
        return "redirect:/admin";
    }

//    @GetMapping("/restUserList")
//    @ResponseBody
//    public List<User> showUserList() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/restUser")
//    @ResponseBody
//    public User showUserById(@RequestParam long id) {
//        return userService.getUserById(id);
//    }
//
//    @GetMapping("/restUserRoles")
//    @ResponseBody
//    public List<UserRole> showUserRoles() {
//        return userRoleService.getAllUserRoles();
//    }
}
