package com.example.invc_proj.repository;

import com.example.invc_proj.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Login, Integer>
{}
