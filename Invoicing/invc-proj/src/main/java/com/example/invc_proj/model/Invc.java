package com.example.invc_proj.model;



import java.util.Date;
import java.util.List;

public class Invc {
    private int client_id;
    private int user_id;
    private Date last_updated_date;
    private List<InvoiceServices> invcSrvcs;
    private int TDS_percent;
    private int TDS_cost;
    private int GST_percent;
    private int GST_cost;
    private int total;

    }

