package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ClientDTO;
import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
import com.example.invc_proj.model.Client;
import com.example.invc_proj.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client-config")
@PreAuthorize("isAuthenticated()")
public class ClientController {

    @Autowired
    private ClientService service;


    @GetMapping("/clients")
    public ResponseEntity<ApiResponse<List<ClientDropdownDTO>>> getClientsForDropdown()
    {
       List<ClientDropdownDTO> clientDropdownDTO =  service.getClientsForDropdown();
        return ApiResponses.ok(clientDropdownDTO);
    }

    // Get Client by ID
   @GetMapping("/client/{ClientId}")
    public ResponseEntity<ApiResponse<Client>> getClientById(@PathVariable int ClientId) {
        Client client = service.getClientById(ClientId);
            return ApiResponses.ok(client);
    }

   /* @GetMapping("/client/{ClientId}")
    public ResponseEntity<ClientDTO> getClientDTOById(@PathVariable int ClientId) {
        ClientDTO client = service.getClientDTOById(ClientId);
        return ResponseEntity.ok(client);
    }
*/
    // Add a new client
    @PostMapping("/client")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> addClient(@RequestBody ClientDTO clientDTO) {
        service.addClient(clientDTO);
        return ApiResponses.created("Client added successfully");
    }

    // Update an existing client
    @PutMapping("/client")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> alterClient(@RequestBody Client client)
    {
        Client existingClient = service.getClientById(client.getClient_id());
        service.updateClient(client);  // Rename to updateClient if necessary
        return ApiResponses.ok("Client updated successfully");
    }

    @DeleteMapping("/client/{p_client_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse<String>> deleteclient(@PathVariable int p_client_id)
    {
        service.removeClient(p_client_id);
        return ApiResponses.ok("Client Removed Successfully");
    }



    /*@GetMapping("/clients")
    public List<Client> getAllClients()
    {
        return service.getAllClients();
    }

@GetMapping("/clients/{id}")
    public Optional<Client> getClientById(@PathVariable int id)
    {
        System.out.println("calling getClientById");
        return service.getClientById(id);
    }

@PostMapping("/client")
    public void addClients(@RequestBody Client clnt)
    {
        System.out.println("calling post");
        System.out.println(clnt);
        service.addClients(clnt);
    }

*/
}
