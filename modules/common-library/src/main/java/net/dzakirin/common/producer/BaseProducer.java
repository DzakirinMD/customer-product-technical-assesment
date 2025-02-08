package net.dzakirin.common.producer;

import lombok.CustomLog;
import net.dzakirin.common.dto.event.EventWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

@CustomLog
public abstract class BaseProducer<T> {

    @Value("${spring.application.name}")
    private String appName;

    private final String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    protected BaseProducer(
            String topic,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(String key, T dto, String eventType) {
        EventWrapper<T> eventWrapper = EventWrapper.<T>builder()
                .eventId(key)
                .eventType(eventType)
                .eventSource(appName)
                .timestamp(LocalDateTime.now())
                .payload(dto)
                .build();
        try {
            kafkaTemplate.send(topic, key, eventWrapper);
        } catch (Exception e) {
            log.error(String.format("Failed to produce event for topic %s: %s", topic, dto), e);
        }
    }
}

