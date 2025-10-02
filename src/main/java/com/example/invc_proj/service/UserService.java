package com.example.invc_proj.service;

import com.example.invc_proj.dto.PasswordUpdateRequestDTO;
import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.exceptions.InvalidOldPasswordException;
import com.example.invc_proj.exceptions.InvalidPasswordLengthException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {


    private final UserRepo User_repo;
    private final RoleRepository role_repo;
    private final EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo User_repo,RoleRepository role_repo, EmailService emailService)
    {
     this.User_repo = User_repo;
     this.role_repo = role_repo;
     this.emailService = emailService;
        //this.userRepository = userRepository;
    }

    public List<User> getUsers()
    {
      return User_repo.findAll();
    }

   /* public Optional<User> getUserById(int user_id)
    {
        return User_repo.findById(user_id);
    }

    public Optional<User> getUserByName(String UserName)
    {
        return Optional.ofNullable(User_repo.findByUserName(UserName));
    }*/

    public Optional<User> getUserById(int user_id)
    {

        return Optional.ofNullable(User_repo.findById(user_id).
                orElseThrow(() -> new RuntimeException("User not found")));

    }

    public Optional<User> getUserByName(String UserName)
    {
        return Optional.ofNullable(User_repo.findByUsername(UserName).orElseThrow(() -> new RuntimeException("User not found")));

    }

    public ResponseEntity<String> addUser(UserDTO user)
    {
        User NewUser = new User();
        NewUser.setUsername(user.getUsername());
        NewUser.setFirstName(user.getFirstName());
        NewUser.setLastName(user.getLastName());
        AppRole role = role_repo.findById(user.getRole_id()).orElseThrow(() ->new RuntimeException("Role Not Found"));
        NewUser.setRole(role);
        NewUser.setStatus(user.getStatus());
        String rawPassword = user.getPassword();
        //Validate password strength method needs to added and few more validators, to have strong password
        if (user.getPassword().length() < 8)
        {
            //return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
            throw new InvalidPasswordLengthException("Password must be at least 8 characters long");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        NewUser.setPassword(encodedPassword);
        System.out.println(user.getPassword());
        //NewUser.setPasswordSalt(user.getPasswordSalt());
        NewUser.setPasswordSalt(rawPassword);

        if(User_repo.existsByEmailId(user.getEmailId())) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            throw new RuntimeException("Email Id Already Exists");
        }

        NewUser.setEmailId(user.getEmailId());

        System.out.println(NewUser);
        User_repo.save(NewUser);
        System.out.println(NewUser);
        emailService.sendWelcomeEmail(NewUser.getEmailId(),NewUser.getUsername());

        return ResponseEntity.ok("User added successfully");
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder2;
    //private final User_repo userRepository;

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
        //Validate password strength method needs to added and few more validators, to have strong password
        if (user.getPassword().length() < 8)
        {
            //return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
            throw new InvalidPasswordLengthException("Password must be at least 8 characters long");
        }
        if(User_repo.existsByEmailId(user.getEmailId())) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            throw new RuntimeException("Email Id Already Exists");
        }
        User_repo.save(user);
    }


    public ResponseEntity<String> updatePassword(PasswordUpdateRequestDTO request, Principal principal){
        //User user = User_repo.findByUserName(principal.getName());

        User user = User_repo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify the old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
        {
            throw new InvalidOldPasswordException("Old password is incorrect");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }

        // Validate new password strength need to include few more validators to have strong password
        if (request.getNewPassword().length() < 8)
        {
            throw new InvalidPasswordLengthException("Password must be at least 8 characters long");
        }

        // Hash the new password before saving
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User_repo.save(user);
        return ResponseEntity.status(201).body("Password updated successfully");
    }

}
