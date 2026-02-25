package com.example.invc_proj.repository;

import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<ClientDropdownDTO> findClientsForDropdown();

    -- Trigram index for fast ILIKE search on name, mobile, email
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_clients_name_trgm    ON clients USING GIN (client_name gin_trgm_ops);
CREATE INDEX idx_clients_mobile_trgm  ON clients USING GIN (primary_mobile_number gin_trgm_ops);
CREATE INDEX idx_clients_email_trgm   ON clients USING GIN (primary_email_id gin_trgm_ops);


    */

    @Query("SELECT new com.example.invc_proj.dto.ClientDropdownDTO" +
            "(c.client_id, c.client_name, c.client_type) " +
            "FROM Client c")
    List<ClientDropdownDTO> findClientsForDropdown();

    Optional<Client> findByUpiIdOrAccountNumber(String upiId, String accountNumber);

    @Query(value = "SELECT new com.example.invc_proj.dto.ClientDropdownDTO" +
            "(c.client_id, c.client_name, c.client_type)  " +
            "FROM Client c " +
            "WHERE c.client_name IN :clientName "
    )
    Page<ClientDropdownDTO> findByClientByName(
            @Param("clientName") String clientName,
            Pageable pageable);


    @Query(value = """
            SELECT client_id, client_name, client_type
            FROM clients
            WHERE :q = ''
               OR client_name ILIKE :pattern
               OR primary_mobile_number ILIKE :pattern
               OR primary_email_id ILIKE :pattern
            ORDER BY client_name ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> searchForLov(@Param("q") String q,
                                @Param("pattern") String pattern,
                                @Param("limit") int limit);

}
