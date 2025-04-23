package ru.home_work.t1_school.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class KafkaMessageProducerTest {
    private final KafkaTemplate kafkaTemplate = mock(KafkaTemplate.class);
    private KafkaMessageProducer producer;

    @BeforeEach
    void setUp() {
        producer = new KafkaMessageProducer(kafkaTemplate);
    }

    @Test
    void sendMessage() {
        producer.sendTo(anyString(), any());
        verify(kafkaTemplate).send(anyString(), any());
        verify(kafkaTemplate).flush();
    }

    @Test
    void sendMessageThrowsException() {
        when(kafkaTemplate.send(anyString(), any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> producer.sendTo(anyString(), any()));
        verify(kafkaTemplate, never()).flush();
    }
}