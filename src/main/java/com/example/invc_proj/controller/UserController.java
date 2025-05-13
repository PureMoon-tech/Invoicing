package com.example.invc_proj.controller;

import com.example.invc_proj.dto.PasswordUpdateRequestDTO;
import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.model.userLogin;
import com.example.invc_proj.model.User;
import com.example.invc_proj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-config")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public List<User> getUsers() {
       return service.getUsers();
   }

    @GetMapping("/users/{UserId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable int UserId)
    {
        Optional<User> users = service.getUserById(UserId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-name/{UserName}")
    public ResponseEntity<Optional<User>> getUserByName(@PathVariable String UserName)
    {
        Optional<User> users = service.getUserByName(UserName);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody UserDTO user)
    {
        System.out.println("calling add user"+user);
        return service.addUser(user);
        //return ResponseEntity.status(201).body("user added successfully");
    }

    @PutMapping("/users")
    public ResponseEntity<String> alterUser(@RequestBody User user)
    {
        System.out.println("calling add user"+user);
        service.alterUser(user);
        return ResponseEntity.status(201).body("user altered successfully");
    }

   /* @PutMapping("/user")
    public ResponseEntity<String> updatePassword(@RequestBody User user)
    {
        System.out.println("calling add user"+user);
        service.alterUser(user);
        return ResponseEntity.status(201).body("user altered successfully");
    }*/
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequestDTO request, Principal principal)
    {
        return service.updatePassword(request,principal);
    }

}





