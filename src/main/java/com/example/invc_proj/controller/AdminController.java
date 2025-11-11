package com.example.invc_proj.controller;

import com.example.invc_proj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/migrate-passwords")
    public String migratePasswords() {
       // userService.encryptExistingPasswords();
        return "Passwords updated successfully.";
    }
}
