package com.anupa.paymentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/internal")
public class MainRestController {

    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);
    @Autowired
    TokenService tokenService;
    @Autowired
    PaymentRepository paymentRepository;

    @PostMapping("payment/create/{orderid}/{amount}") // Payments should be mapped to Order Id not to Project Id!!!
    ResponseEntity<?> createPayment(@PathVariable("orderid") String orderid,
                                    @PathVariable("amount") Integer amount,
                                    Payment payment, // invoked by project service
                                    @RequestHeader("Authorization") String token) throws InterruptedException {

        logger.info("Request received for order payment against order {} for amount {}: ",orderid,amount);
        // TOKEN VALIDATION IS REQUIRED
        //Optional<String> token =  Optional.ofNullable(tokenService.getAuthCookieValue(request));
        logger.info("Token extracted for order payment {}: ",token);
        Optional<String> principal =  Optional.ofNullable(tokenService.validateToken(token));
        logger.info("Principal extracted for order payment {}: ",principal);
        // Validate the token (omitted for brevity)

        if(principal.isPresent())
        {
            Thread.sleep(10000);

            payment.setPayerPhone(principal.get());
            payment.setOrderId(orderid);
            payment.setStatus("PENDING");
            payment.setAmount(amount);
            Payment savedPayment = paymentRepository.save(payment);

            logger.info("Saved Payment with id {} against order {}: ",savedPayment.getId(),orderid);

            return ResponseEntity.ok().body(savedPayment.getId());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

}
