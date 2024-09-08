package com.example.invc_proj.repository;


import com.example.invc_proj.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>
{
}

