package com.example.invc_proj.repository;

import com.example.invc_proj.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActvityLogRepo extends JpaRepository<ActivityLog, Long> {

}
