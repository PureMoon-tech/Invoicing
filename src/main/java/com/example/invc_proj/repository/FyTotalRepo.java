package com.example.invc_proj.repository;

import com.example.invc_proj.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FyTotalRepo extends JpaRepository <Client, Long>
{


    @Query(value = "SELECT public.get_current_fy_total(:p_client_id)", nativeQuery = true)
    int get_current_fy_total(@Param("p_client_id") Integer param);


}