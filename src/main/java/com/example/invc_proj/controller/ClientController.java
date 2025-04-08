package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.model.Client;
import com.example.invc_proj.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client-config")
public class ClientController {

    @Autowired
    private ClientService service;


    @GetMapping("/clients")
    public ResponseEntity<List<ClientDropdownDTO>> getClientsForDropdown()
    {
       List<ClientDropdownDTO> clientDropdownDTO =  service.getClientsForDropdown();
        return ResponseEntity.ok(clientDropdownDTO);
    }

    // Get Client by ID
    @GetMapping("/client/{ClientId}")
    public ResponseEntity<Client> getClientById(@PathVariable int ClientId) {
        Optional<Client> client = service.getClientById(ClientId);
        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Add a new client
    @PostMapping("/client")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        service.addClient(client);
        return ResponseEntity.status(201).body("Client added successfully");
    }

    // Update an existing client
    @PutMapping("/client")
    public ResponseEntity<String> alterClient(@RequestBody Client client)
    {
        // Check if client exists, if not return 404 Not Found
        Optional<Client> existingClient = service.getClientById(client.getClient_id());
        if (existingClient.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        service.updateClient(client);  // Rename to updateClient if necessary
        return ResponseEntity.ok("Client updated successfully");
    }

    @DeleteMapping("/client/{p_client_id}")
    public  ResponseEntity<String> deleteclient(@PathVariable int p_client_id)
    {
        service.removeClient(p_client_id);
        return ResponseEntity.ok("Client Removed Successfully");
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
