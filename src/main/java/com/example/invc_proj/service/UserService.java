package com.example.invc_proj.service;

import com.example.invc_proj.model.USERS;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
 private final UserRepo User_repo;


 public UserService(UserRepo User_repo){
     this.User_repo = User_repo;
 }

    public List<USERS> getUsers()
    {
      return User_repo.findAll();
    }

    public Optional<USERS> getUserById(int user_id)
    {
        return User_repo.findById(user_id);
    }

    public Optional<USERS> getUserByName(String UserName)
    {
        return Optional.ofNullable(User_repo.findByUserName(UserName));
    }

    public void addUser(USERS user)
    {
       User_repo.save(user);
    }


}
