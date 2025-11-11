package com.example.invc_proj.service;

import com.example.invc_proj.dto.ServicesDTO;
import com.example.invc_proj.dto.ServicesDropdownDTO;
import com.example.invc_proj.mapper.ServicesDTOMapper;
import com.example.invc_proj.mapper.ServicesDropdownDTOMapper;
import com.example.invc_proj.model.Services;
import com.example.invc_proj.repository.ServicesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServicesRepo servicesRepo;
    
    public List<Services> getAllServices()
    {
        return servicesRepo.findAll();
    }

    public List<ServicesDropdownDTO> getAllServicesDropdown()
    {
        List<Services> services = servicesRepo.findAll();
        List<ServicesDropdownDTO> servicesDropdownDTOS = services.stream()
                .map(ServicesDropdownDTOMapper::toDTO)
                .toList();
        return servicesDropdownDTOS;
    }

    public Services getServiceById(int id)
    {
        return servicesRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Service not found"));
    }

    public ServicesDropdownDTO getServiceDropdownById(int id)
    {
        Services service = servicesRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Service not found"));
        return ServicesDropdownDTOMapper.toDTO(service);
    }

    public void addServices(ServicesDTO srvc)
    {
        Services services = ServicesDTOMapper.mapToService(srvc);
        servicesRepo.save(services);
    }

    public void updateServiceStatus(int pServiceId)
    {
        Services services = servicesRepo.findById(pServiceId).
                orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public void deleteService(int pServiceId)
    {
        servicesRepo.deleteById(pServiceId);
    }
}
