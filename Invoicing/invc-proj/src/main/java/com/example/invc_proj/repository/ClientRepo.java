package com.example.invc_proj.repository;

import com.example.invc_proj.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer>
{
}
