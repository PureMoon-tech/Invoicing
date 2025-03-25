package com.example.invc_proj.controller;


import com.example.invc_proj.model.*;
import com.example.invc_proj.service.Process_Invoice;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Services> getAllServices()
    {
        return service.getAllServices();
    }

@GetMapping("/services/{id}")
    public Optional<Services> getServiceById(@PathVariable int id)
    {
        System.out.println("calling getServiceById");
        return service.getServiceById(id);
    }

@PostMapping("/services")
    public void addServices(@RequestBody Services srvc)
    {
        System.out.println("calling post");
        System.out.println(srvc);
        service.addServices(srvc);
    }


}
