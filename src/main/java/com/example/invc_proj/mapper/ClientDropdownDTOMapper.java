package com.example.invc_proj.mapper;

import com.example.invc_proj.model.Client;
import com.example.invc_proj.dto.ClientDropdownDTO;

public class ClientDropdownDTOMapper
{

        // Method to convert Client entity to ClientDropdownDTO
        public static ClientDropdownDTO toClientDropdownDTO(Client client)
        {
            // Check if the client is null to avoid NullPointerException
            if (client == null)
            {
                return null;
            }

            // Create a new ClientDropdownDTO and map values from Client entity
            return new ClientDropdownDTO(
                    client.getClient_id(),
                    client.getClient_name(),
                    client.getClient_type());
        }

        // Method to convert ClientDropdownDTO to Client entity (optional, if needed)
        public static Client toClient(ClientDropdownDTO clientDropdownDTO)
        {
            // Check if the clientDropdownDTO is null
            if (clientDropdownDTO == null) {
                return null;
            }

            // Create a new Client entity and map values from ClientDropdownDTO
            Client client = new Client();
            client.setClient_id(clientDropdownDTO.getClient_id());
            client.setClient_name(clientDropdownDTO.getClient_name());
            client.setClient_type(clientDropdownDTO.getClient_type());

            // Return the created Client entity
            return client;
        }
}
