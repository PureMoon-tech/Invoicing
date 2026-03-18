package com.example.invc_proj.controller;

import com.example.invc_proj.dto.PasswordUpdateRequestDTO;
import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.dto.UserDropdownDTO;
import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
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
@RequestMapping("/api/user-config")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDropdownDTO>>> getUsers() {
       return ApiResponses.ok(service.getUsersDropdown());
   }

    @GetMapping("/users/{UserId}")
    public ResponseEntity<ApiResponse<Optional<User>>> getUserById(@PathVariable int UserId)
    {
        Optional<User> users = service.getUserById(UserId);
        return ApiResponses.ok(users);
    }

    @GetMapping("/user-name/{UserName}")
    public ResponseEntity<ApiResponse<Optional<User>>> getUserByName(@PathVariable String UserName)
    {
        Optional<User> users = service.getUserByName(UserName);
        return ApiResponses.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody UserDTO user)
    {
        return ApiResponses.created(service.addUser(user));

    }

    @PatchMapping("/users")
    public ResponseEntity<ApiResponse<String>> alterUser(@RequestBody UserDTO user)
    {

        return ApiResponses.ok(service.alterUser(user));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@RequestBody PasswordUpdateRequestDTO request, Principal principal)
    {
        return ApiResponses.ok(service.updatePassword(request,principal));
    }

    @DeleteMapping("/users/{UserId}")
    public ResponseEntity<ApiResponse<String>> removeUserById(@PathVariable int UserId)
    {

        return ApiResponses.ok(service.removeUserById(UserId));
    }

}





