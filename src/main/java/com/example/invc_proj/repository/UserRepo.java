package com.example.invc_proj.repository;


import com.example.invc_proj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>
{
        //System.out.println("User Not Found "+USERS);
        @Query(value = "SELECT * FROM users WHERE user_name = :username", nativeQuery = true)
        User findByUserName(String username);

    Optional<User> findById(Integer user_Id);




}
