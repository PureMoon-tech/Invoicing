package com.example.invc_proj.model.Enum;


public enum InvoiceStatus {
    UNPAID,
    PARTIALLY_PAID,
    PAID;

    public boolean equalsIgnoreCase(String unpaid)
    {
       return true ;//equalsIgnoreCase(unpaid);
    }
}
