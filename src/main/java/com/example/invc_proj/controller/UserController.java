package com.example.invc_proj.controller;

import com.example.invc_proj.model.userLogin;
import com.example.invc_proj.model.USERS;
import com.example.invc_proj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-config")
public class UserController {

    @Autowired
    private UserService service;

/*    @GetMapping("/users/")
    public List<USERS> getUsers()
    {
      return service.getUsers();
    }*/

    @GetMapping("/user/{UserId}")
    public Optional<USERS> getUserById(@PathVariable int UserId)
    {
        return service.getUserById(UserId);
    }

    @GetMapping("/user-name/{UserName}")
    public Optional<USERS> getUserByName(@PathVariable String UserName)
    {
        return service.getUserByName(UserName);
    }

    @PostMapping("/user")
    public void addUser(@RequestBody USERS user)
    {
        System.out.println("calling add user"+user);
        service.addUser(user);
    }

    @PutMapping("/User")
    public void alterUser(@RequestBody USERS user)
    {
        System.out.println("calling add user"+user);
        service.addUser(user);
    }
}
