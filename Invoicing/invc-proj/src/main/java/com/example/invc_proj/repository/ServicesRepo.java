package com.example.invc_proj.repository;


import com.example.invc_proj.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface ServicesRepo extends JpaRepository<Services, Integer>
    {
    }


