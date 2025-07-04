package com.example.invc_proj.controller;


import com.example.invc_proj.dto.ServicesDTO;
import com.example.invc_proj.model.*;
import com.example.invc_proj.service.Process_Invoice;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service-config")
@PreAuthorize("isAuthenticated()")
public class ServicesController
{

    @Autowired
    private ProductService service;

        @GetMapping("/services")
            public ResponseEntity<List<Services>> getAllServices()
            {

                List<Services> services = service.getAllServices();
                return ResponseEntity.ok(services);
            }

        @GetMapping("/services/{id}")
            public ResponseEntity<Optional<Services>> getServiceById(@PathVariable int id)
            {
                Optional<Services> services = service.getServiceById(id);
                return ResponseEntity.ok(services);
            }

        @PostMapping("/services")
        @PreAuthorize("hasRole('ADMIN')")
            public ResponseEntity<String> addServices(@RequestBody ServicesDTO srvc)
            {
                service.addServices(srvc);
                return ResponseEntity.status(201).body("Service added successfully");
            }

        @PutMapping("/services/{p_service_id}")
        @PreAuthorize("hasRole('ADMIN')")
             public ResponseEntity<String> updateServiceStatus(@PathVariable int p_service_id)
            {
               service.updateServiceStatus(p_service_id);
               return ResponseEntity.status(200).body("Service status updated successfully");
            }

        @DeleteMapping("/services/{p_service_id}")
        @PreAuthorize("hasRole('ADMIN')")
            public ResponseEntity<String> deleteService(@PathVariable int p_service_id)
           {
             service.deleteService(p_service_id);
             return ResponseEntity.status(200).body("service deleted successfully");
           }

}
