package com.example.invc_proj.repository;


import com.example.invc_proj.model.USERS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<USERS, Integer>
{
        //System.out.println("User Not Found "+USERS);
    USERS findByUserName(String userName);

    Optional<USERS> findById(Integer user_Id);


}
