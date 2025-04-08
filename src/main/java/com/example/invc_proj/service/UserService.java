package com.example.invc_proj.service;

import com.example.invc_proj.model.User;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
 private final UserRepo User_repo;


 public UserService(UserRepo User_repo){
     this.User_repo = User_repo;
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

    public void addUser(User user)
    {
       User_repo.save(user);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder2;

    public void encryptExistingPasswords() {
        List<User> users = User_repo.findAll();

        for (User user : users) {
            String rawPassword = user.getPassword();

            // Skip already encoded passwords
            if (rawPassword != null && !rawPassword.startsWith("$2a$")) {
                String encodedPassword = passwordEncoder2.encode(rawPassword);
                user.setPassword(encodedPassword);
            }
        }

        User_repo.saveAll(users);
    }


}
