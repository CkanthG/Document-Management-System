package com.sree.document.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.sree.document.constant.Constants.CONSUMER_GROUP_ID;
import static com.sree.document.constant.Constants.CONSUMER_KEY_DESERIALIZER_KEY;
import static com.sree.document.constant.Constants.CONSUMER_VALUE_DESERIALIZER_KEY;
import static com.sree.document.constant.Constants.PRODUCER_VALUE_SERIALIZER_KEY;
import static com.sree.document.constant.Constants.PRODUCER_KEY_SERIALIZER_KEY;
import static com.sree.document.constant.Constants.BOOTSTRAP_SERVERS_KEY;

/**
 * This class is used for produce and consume the kafka topics.
 */
@Service
public class KafkaProducerAndConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServersValue;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String consumerKeyDeserializerValue;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String consumerValueDeserializerValue;
    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupIdValue;
    @Value("${spring.kafka.producer.key-serializer}")
    private String producerKeySerializerValue;
    @Value("${spring.kafka.producer.value-serializer}")
    private String producerValueSerializerValue;
    @Value("${spring.kafka.template.default-topic}")
    private String topicValue;

    /**
     * This method build common properties
     * @return gives common properties.
     */
    public Properties commonProperties() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_KEY, bootstrapServersValue); // Change to your Kafka broker(s)
        return props;
    }

    /**
     * This method is accepting kafka key and value to produce a message.
     * @param key
     * @param value
     */
    public void kafkaProducer(String key, String value) {
        Properties props = commonProperties();
        props.put(PRODUCER_KEY_SERIALIZER_KEY, producerKeySerializerValue);
        props.put(PRODUCER_VALUE_SERIALIZER_KEY, producerValueSerializerValue);
        // Create KafkaProducer instance
        Producer<String, String> producer = new KafkaProducer<>(props);

        // Produce messages
        ProducerRecord<String, String> record = new ProducerRecord<>(topicValue, key, value);
        producer.send(record, (metadata, exception) -> {
            if (metadata != null) {
                System.out.println("Message sent successfully: " + metadata.topic() +
                        ", partition: " + metadata.partition() + ", offset: " + metadata.offset());
            } else {
                System.err.println("Error sending message: " + exception.getMessage());
            }
        });
        // Close the producer
        producer.close();
        System.out.println("Producer Closed");
    }

    /**
     * This method is used for consume a kafka topic to get key and value which is produced earlier.
     */
    public void kafkaConsumer() {
        Properties props = commonProperties();
        props.put(CONSUMER_KEY_DESERIALIZER_KEY, consumerKeyDeserializerValue);
        props.put(CONSUMER_VALUE_DESERIALIZER_KEY, consumerValueDeserializerValue);
        props.put(CONSUMER_GROUP_ID, consumerGroupIdValue);

        // Create KafkaConsumer instance
        Consumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to topic
        consumer.subscribe(Collections.singletonList(topicValue));

        // Poll for messages
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            System.out.println("Records count : " + records.count());
            System.out.println("Records : " + records);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received message: key = " + record.key() + ", value = " + record.value() + ", " +
                                "partition = " + record.partition() + ", offset = " + record.offset());
            }
        }
    }
}
