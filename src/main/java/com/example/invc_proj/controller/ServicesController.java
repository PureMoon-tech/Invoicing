package com.example.invc_proj.controller;


import com.example.invc_proj.model.*;
import com.example.invc_proj.service.Process_Invoice;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service-config")
public class ServicesController
{

    @Autowired
    private ProductService service;

    //private Process_Invoice Prcs_invc;



@GetMapping("/services")
    public ResponseEntity<List<Services>> getAllServices()
    {

        List<Services> services = service.getAllServices();
        return ResponseEntity.ok(services);
    }

@GetMapping("/services/{id}")
    public ResponseEntity<Optional<Services>> getServiceById(@PathVariable int id)
    {
        System.out.println("calling getServiceById");
        Optional<Services> services = service.getServiceById(id);
        return ResponseEntity.ok(services);
    }

@PostMapping("/services")
    public ResponseEntity<String> addServices(@RequestBody Services srvc)
    {
        System.out.println("calling post");
        System.out.println(srvc);
        service.addServices(srvc);
        return ResponseEntity.status(201).body("Service added successfully");
    }


}
