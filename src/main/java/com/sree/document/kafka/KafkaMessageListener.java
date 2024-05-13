package com.sree.document.kafka;

import com.sree.document.models.CustomObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * This class is used for sending message to kafka streams and listening to kafka streams
 */
@Component
@AllArgsConstructor
@Slf4j
public class KafkaMessageListener {

    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * This method is to listen the topic
     * @param message
     */
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listenToTopic(String message) {
        log.info("Received message from Kafka: {}", message);
    }

    /**
     * This method is used to send the message to specific topic
     * @param topic
     * @param message
     */
    public void sendMessageToKafka(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("Sent message to Kafka: {} on Topic: {}", message, topic);
    }
}
