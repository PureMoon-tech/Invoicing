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
import java.util.Map;
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


    // Add a new client
    @PostMapping("/client")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> addClient(@RequestBody ClientDTO clientDTO) {
        service.addClient(clientDTO);
        return ApiResponses.created("Client added successfully");
    }


    @PatchMapping("/client")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> alterClient(@RequestBody ClientDTO clientDTO)
    {
        return ApiResponses.ok(service.updateClient(clientDTO));
    }

    @DeleteMapping("/client/{p_client_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse<String>> deleteclient(@PathVariable int p_client_id)
    {
        service.removeClient(p_client_id);
        return ApiResponses.ok("Client Removed Successfully");
    }

    @GetMapping("/lov")
    public ResponseEntity<Map<String, Object>> getLov(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit) {

        List<ClientDropdownDTO> data = service.searchLov(q, limit);

        return ResponseEntity.ok(Map.of(
                "data", data,
                "count", data.size()
        ));
    }



}
