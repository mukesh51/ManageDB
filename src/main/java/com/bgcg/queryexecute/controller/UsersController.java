package com.bgcg.queryexecute.controller;


import com.bgcg.queryexecute.entity.Users;
import com.bgcg.queryexecute.entity.UsersType;
import com.bgcg.queryexecute.exception.UserAlreadyExistsException;
import com.bgcg.queryexecute.services.UsersService;
import com.bgcg.queryexecute.services.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UsersController {

    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/manage/passwords")
    public String managePasswords(Model model) {
        List<Users> allUsers = usersService.getAllUsers();
        model.addAttribute("getAllUsers", allUsers);
        model.addAttribute("user", new Users());
        return "managePasswords";
    }

    @PostMapping("/password/update/confirmation")
    public String passwordUpdateConfirmation(Users users, Model model) {
        try {
            usersService.updateUserPassword(users);
            model.addAttribute("confirmationMessage", "User's password updated successfully");
            List<Users> allUsers = usersService.getAllUsers();
            model.addAttribute("getAllUsers", allUsers);
            model.addAttribute("user", new Users());
            return "managePasswords";
        } catch (Exception e) {
            model.addAttribute("confirmationMessage", "Something went wrong while updating user's password.");
            List<Users> allUsers = usersService.getAllUsers();
            model.addAttribute("getAllUsers", allUsers);
            model.addAttribute("user", new Users());
            return "managePasswords";
        }
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes = usersTypeService.getAll();
        List<UsersType> filteredUsersTypes = usersTypes.stream()
                .filter(userType -> "Staff".equals(userType.getUserTypeName()))
                .collect(Collectors.toList());
        model.addAttribute("getAll", filteredUsersTypes);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model){
        try {
            usersService.addNew(users);
            return "login";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<UsersType> usersTypes = usersTypeService.getAll();
            List<UsersType> filteredUsersTypes = usersTypes.stream()
                    .filter(userType -> "Staff".equals(userType.getUserTypeName()))
                    .collect(Collectors.toList());
            model.addAttribute("getAll", filteredUsersTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!= null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "recirect:/";

    }




}
