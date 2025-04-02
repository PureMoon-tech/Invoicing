package com.example.invc_proj.controller;

import com.example.invc_proj.model.userLogin;
import com.example.invc_proj.model.USERS;
import com.example.invc_proj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Optional<USERS>> getUserById(@PathVariable int UserId)
    {
        Optional<USERS> users = service.getUserById(UserId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-name/{UserName}")
    public ResponseEntity<Optional<USERS>> getUserByName(@PathVariable String UserName)
    {
        Optional<USERS> users = service.getUserByName(UserName);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody USERS user)
    {
        System.out.println("calling add user"+user);
        service.addUser(user);
        return ResponseEntity.status(201).body("user added successfully");
    }

    @PutMapping("/User")
    public ResponseEntity<String> alterUser(@RequestBody USERS user)
    {
        System.out.println("calling add user"+user);
        service.addUser(user);
        return ResponseEntity.status(201).body("user altered successfully");
    }
}
