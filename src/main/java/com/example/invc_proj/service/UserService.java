package com.example.invc_proj.service;

import com.example.invc_proj.dto.PasswordUpdateRequestDTO;
import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.model.AppRole;
import com.example.invc_proj.model.User;
import com.example.invc_proj.repository.RoleRepository;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
     private final UserRepo User_repo;

    @Autowired
    private final RoleRepository role_repo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserService(UserRepo User_repo,RoleRepository role_repo)
    {
     this.User_repo = User_repo;
     this.role_repo = role_repo;
    }

    public List<User> getUsers()
    {
      return User_repo.findAll();
    }

    public Optional<User> getUserById(int user_id)
    {

        return User_repo.findById(user_id);
    }

    public Optional<User> getUserByName(String UserName)
    {
        return Optional.ofNullable(User_repo.findByUserName(UserName));
    }

    public ResponseEntity<String> addUser(UserDTO user)
    {
        User NewUser = new User();
        NewUser.setUsername(user.getUsername());
        NewUser.setFirstName(user.getUsername());
        NewUser.setLastName(user.getUsername());
        AppRole role = role_repo.findById(user.getRole_id()).orElseThrow(() ->new RuntimeException("Role Not Found"));
        NewUser.setRole(role);
        NewUser.setStatus(user.getStatus());
        String rawPassword = user.getPassword();
        //Validate password strength method needs to added and few more validators, to have strong password
        if (user.getPassword().length() < 8)
        {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        NewUser.setPassword(encodedPassword);
        System.out.println(user.getPassword());
        NewUser.setPasswordSalt(user.getPasswordSalt());
        System.out.println(NewUser);
        User_repo.save(NewUser);
        System.out.println(NewUser);

        return ResponseEntity.ok("User added successfully");
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder2;

    public void encryptExistingPasswords()
    {
        List<User> users = User_repo.findAll();

        for (User user : users) {
            String rawPassword = user.getPassword();

            // Skip already encoded passwords
            if (rawPassword != null && !rawPassword.startsWith("$2a$"))
            {
                String encodedPassword = passwordEncoder2.encode(rawPassword);
                user.setPassword(encodedPassword);
            }
        }

        User_repo.saveAll(users);
    }


    public void alterUser(User user)
    {
        User_repo.save(user);
    }


    public ResponseEntity<String> updatePassword(PasswordUpdateRequestDTO request, Principal principal){
        User user = User_repo.findByUserName(principal.getName());

        // Verify the old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }

        // Validate new password strength need to include few more validators to have strong password
        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
        }

        // Hash the new password before saving
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User_repo.save(user);
        return ResponseEntity.status(201).body("Password updated successfully");
    }

}
