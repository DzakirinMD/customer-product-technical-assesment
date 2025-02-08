package net.dzakirin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.common.dto.request.EmailRequest;
import net.dzakirin.service.EmailService;
import net.dzakirin.service.OrderProcessingService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/emails")
public class EmailController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping("/send-loyalty-points")
    public void sendLoyaltyPointsEmail(@RequestBody EmailRequest emailRequest) {
        log.info("Received request to send loyalty points email: {}", emailRequest);
        orderProcessingService.sendLoyaltyPointsEmail(emailRequest);
    }
}
