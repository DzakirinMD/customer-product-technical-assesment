package net.dzakirin.producer;

import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.common.producer.BaseProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDataChangedProducer extends BaseProducer<OrderEvent> {

    public OrderDataChangedProducer(
            @Value("${kafka.producer.topic.order-data-changed}") String topic,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        super(topic, kafkaTemplate);
    }
}
