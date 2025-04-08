package com.example.invc_proj.repository;

import com.example.invc_proj.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Integer> {

    @Query(value = "Select * from App_Roles where role_name = :roleName",nativeQuery = true)
    Optional<AppRole> findByName(String roleName);

    @Query(value = "select * from App_Roles where role_id = :roleid",nativeQuery = true)
    Optional<AppRole> findById(int roleid);
}

