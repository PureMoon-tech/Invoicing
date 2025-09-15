package com.example.ecom_proj.service;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.*;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Enum.InvoiceType;
import com.example.invc_proj.repository.ClientRepo;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ServicesRepo;
import com.example.invc_proj.repository.ServicesRequestedRepo;
import com.example.invc_proj.service.InvoiceNumGeneratorService;
import com.example.invc_proj.service.InvoiceService;
import com.example.invc_proj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @Mock private ClientRepo clientRepo;
    @Mock private ServicesRepo servicesRepo;
    @Mock private InvoiceRepo invoiceRepo;
    @Mock private ServicesRequestedRepo servicesRequestedRepo;
    @Mock private UserService userService;
    @Mock private InvoiceNumGeneratorService invoiceNumGeneratorService;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContextHolder
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUsername()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userPrincipal, null)
        );
    }

    @Test
    public void testGenerateInvoice() {
        int clientId = 1;
        int bankId = 2;
        BigDecimal amountPaid = new BigDecimal("100.00");
        InvoiceStatus status = InvoiceStatus.PARTIALLY_PAID;
        InvoiceType type = InvoiceType.SERVICE;

        // Mock user
        User user = new User();
        user.setId(10);
        when(userService.getUserByName("testuser")).thenReturn(Optional.of(user));

        // Mock client
        Client client = new Client();
        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));

        // Mock service
        Services service = new Services();
        service.setService_id(5);
        when(servicesRepo.findById(5)).thenReturn(Optional.of(service));

        // Mock invoice number
        when(invoiceNumGeneratorService.generateInvoiceNumber(type)).thenReturn("INV-001");

        // Mock invoice save
        Invoice invoiceToSave = new Invoice();
        invoiceToSave.setInvoice_id(100L);
        when(invoiceRepo.save(any(Invoice.class))).thenReturn(invoiceToSave);

        // Prepare service cost request
        ServiceCostRequest scr = new ServiceCostRequest();
        scr.setServiceId(5);
        scr.setServiceCost(new BigDecimal("200.00"));
        List<ServiceCostRequest> serviceCosts = Collections.singletonList(scr);

        // Run
        Invoice result = invoiceService.generateInvoice(clientId, bankId, status, type, amountPaid, serviceCosts);

        // Verify
        assertNotNull(result);
        verify(clientRepo).findById(clientId);
        verify(servicesRepo).findById(5);
        verify(invoiceRepo).save(any(Invoice.class));
        verify(servicesRequestedRepo).saveAll(anyList());
        assertEquals(100L, result.getInvoice_id());
    }
}