package com.client.controller;

import com.client.model.User;
import com.client.model.UserRole;
import com.client.service.UserRoleService;
import com.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class URestController {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public URestController(UserService userService, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/restUserList")
    public List<User> showUserList() {
        return userService.getAllUsers();
    }

    @GetMapping("/restUser")

    public User showUserById(@RequestParam long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/restUserRoles")

    public List<UserRole> showUserRoles() {
        return userRoleService.getAllUserRoles();
    }
}
