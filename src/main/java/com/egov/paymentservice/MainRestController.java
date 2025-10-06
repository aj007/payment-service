package com.egov.paymentservice;

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

    @PostMapping("payment/create/{projectid}/{amount}") // Payments should be mapped to Order Id not to Project Id!!!
    ResponseEntity<?> createPayment(@PathVariable("projectid") String projectId,
                                    @PathVariable("amount") Integer amount,
                                    Payment payment, // invoked by project service
                                    @RequestHeader("Authorization") String token) throws InterruptedException {

        logger.info("Request received to float project");
        // TOKEN VALIDATION IS REQUIRED
        //Optional<String> token =  Optional.ofNullable(tokenService.getAuthCookieValue(request));
        logger.info("Token extracted to float project {}: ",token);
        Optional<String> principal =  Optional.ofNullable(tokenService.validateToken(token));
        logger.info("Principal extracted to float project {}: ",principal);
        // Validate the token (omitted for brevity)

        if(principal.isPresent())
        {
            Thread.sleep(10000);

            payment.setPayerPhone(principal.get());
            payment.setProjectId(projectId);
            payment.setStatus("PENDING");
            payment.setAmount(amount);
            Payment savedPayment = paymentRepository.save(payment);

            logger.info("Saved Payment with id {} against project {}: ",savedPayment.getId(),projectId);

            return ResponseEntity.ok().body(savedPayment.getId());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

}
