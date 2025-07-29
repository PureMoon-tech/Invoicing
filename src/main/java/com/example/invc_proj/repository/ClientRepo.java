package com.example.invc_proj.repository;

import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer>
{

   // Client findByclient_name(String client_name);

    //Optional<Client> findById(Integer client_id);

   /* @Query("SELECT new com.example.invc_proj.dto.ClientDropdownDTO(c.client_id, c.client_name, c.client_type) " +
            "FROM Client c")
    List<ClientDropdownDTO> findClientsForDropdown();*/

    @Query("SELECT new com.example.invc_proj.dto.ClientDropdownDTO(c.client_id, c.client_name, c.client_type) " +
            "FROM Client c")
    List<ClientDropdownDTO> findClientsForDropdown();

    Optional<Client> findByUpiIdOrAccountNumber(String upiId, String accountNumber);

}
