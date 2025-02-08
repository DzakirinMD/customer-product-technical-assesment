package net.dzakirin.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.common.dto.event.EventWrapper;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.service.OrderProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDataChangedConsumer {
    private final ObjectMapper objectMapper;
    private final OrderProcessingService orderProcessingService;

    @KafkaListener(
            topics = "${kafka.consumer.topic.order-data-changed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(@Payload EventWrapper<?> event) {
        try {
            OrderEvent orderEvent = objectMapper.convertValue(event.getPayload(), OrderEvent.class);
            log.info("Received order event: ID={}, Type={}, Source={}, Timestamp={}, Payload={}",
                    event.getEventId(),
                    event.getEventType(),
                    event.getEventSource(),
                    event.getTimestamp(),
                    orderEvent);

            orderProcessingService.sendOrderConfirmationEmail(orderEvent);
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage(), e);
        }
    }
}
