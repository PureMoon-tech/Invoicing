package com.example.invc_proj.service;

import com.example.invc_proj.dto.ClientDTO;
import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.exceptions.NotFoundException;
import com.example.invc_proj.mapper.ClientDTOMapper;
import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepo clientRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo)
    {
        this.clientRepo = clientRepo;
    }

    // Add a new client
    public void addClient(ClientDTO clientDTO) {

        Client client = ClientDTOMapper.mapToClient(clientDTO);
        //client = ClientDTOMapper.mapToClient(clientDTO);
        clientRepo.save(client);
    }

    // Update an existing client
    public boolean updateClient(Client client) {

       // Client client = ClientDTOMapper.mapToClient(clientDTO);

        Optional<Client> existingClient = clientRepo.findById(client.getClient_id());
        if (existingClient.isPresent()) {

            Client updatedClient = existingClient.get();

            updatedClient.setClient_name(client.getClient_name());
            updatedClient.setClient_type(client.getClient_type());
            updatedClient.setPrimary_mobile_number(client.getPrimary_mobile_number());
            updatedClient.setPrimary_email_id(client.getPrimary_email_id());
            updatedClient.setSecondary_mobile_number(client.getSecondary_mobile_number());
            updatedClient.setSecondary_email_id(client.getSecondary_email_id());
            updatedClient.setOccupation(client.getOccupation());
            updatedClient.setPan_no(client.getPan_no());
            updatedClient.setAdhaar_no(client.getAdhaar_no());
            updatedClient.setAddress(client.getAddress());
            updatedClient.setPincode(client.getPincode());
            updatedClient.setGSTN(client.getGSTN());
            updatedClient.setAccountNumber(client.getAccountNumber());
            updatedClient.setUpiId(client.getUpiId());

            clientRepo.save(updatedClient);
            return true;
        } else {
            return false;  // Client not found
        }
    }

    // Get list of clients with only client_id and client_name (for dropdown)
    public List<ClientDropdownDTO> getClientsForDropdown() {

        return clientRepo.findClientsForDropdown();


      /*
        using streams
        List<Client> clients = clientRepo.findAll();

        // Convert the list of Client entities to a list of ClientDropdownDTOs
        return clients.stream()
                .map(client -> new ClientDropdownDTO(client.getClient_id(), client.getClient_name(), client.getClient_type()))
                .collect(Collectors.toList());*/
           /* using mapper
           for (Client client : clients) {
        ClientDropdownDTO dto = ClientDropdownDTOMapper.toClientDropdownDTO(client);

        // Add the mapped DTO to the list
        if (dto != null) {
            clientDropdownDTOList.add(dto);
        }*/

    }

    public Page<ClientDropdownDTO> SearchClient(String clientName,
                                                int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepo.findByClientByName(clientName, pageable);
    }


    // Get full client details by ID
    public Client getClientById(int clientId) {

        //return clientRepo.findById(clientId);

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        return client;
    }

    // Get full client details by ID
    public ClientDTO getClientDTOById(int clientId) {
        return clientRepo.findById(clientId)
                .map(ClientDTOMapper::mapToClientDTO)
                .orElseThrow(() -> new NotFoundException("Client not found"));
    }

    public void removeClient(int pClientId)
    {

        clientRepo.deleteById(pClientId);
    }

/*    public Optional<Client> getClientById(int id)
    {
      System.out.println("get by id"+id);
       return repository_clnt.findById(id);

    }

    public void addClients(Client clnt)
    {
        repository_clnt.save(clnt);
    }
    */


}
