package com.example.invc_proj.service;

public class Process_Invoice {

    /*  1.get invoice data from front end through post call

        2.first fetch invoice number from sequence and add to template/object

        3.from the data fetch client id and query client details
        and add client details to template/object

        4.from the service id fetch service description and cost,
           add to template and re-assign total value and perform insert

        5. repeat 4 as many times the service id's

        6. based on the client type choose tax type, based on the tax type calculate tax and assign to template

           if (client.GSTN == NULL)
           {
             get invoice total in last financial year if it is greater than 1L with the addition of current total then apply 10% TDS else TDS =0
           }
           else
             GST tax = 18%
             add 18% of total to total split GST by 2 and assign to template

         7. Perform insert for Invoice table

         8. Perform insert for Services requested table

         9 . return success message as invoice ready for preview
     */


}
