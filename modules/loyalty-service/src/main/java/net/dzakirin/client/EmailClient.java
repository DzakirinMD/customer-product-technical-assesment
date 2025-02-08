package net.dzakirin.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.common.dto.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailClient {

    private final WebClient webClient;

    @Value("${client.internal.email-service.baseurl}")
    private String baseUrl;

    public void sendLoyaltyPointsEmail(UUID customerId, String customerEmail, int pointsAwarded) {
        String emailUrl = String.format("%s/emails/send-loyalty-points", baseUrl.replaceAll("/$", "")); // Ensure no double slashes

        webClient.post()
                .uri(emailUrl)
                .bodyValue(new EmailRequest(customerId, customerEmail, pointsAwarded))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("✅ Loyalty Points Email Sent: CustomerID={}, Points={}", customerId, pointsAwarded))
                .doOnError(error -> log.error("❌ Failed to send Loyalty Points Email: {}", error.getMessage()))
                .subscribe();
    }
}
