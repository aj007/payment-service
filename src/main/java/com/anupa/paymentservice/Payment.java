package com.anupa.paymentservice;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@Data
public class Payment {


    @Id
    String id;
    String orderId;
    String payerPhone;
    Integer amount;
    String status; // PENDING, COMPLETED, FAILED

}
