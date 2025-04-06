package ru.home_work.t1_school.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.model.MessageDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {
    private final KafkaTemplate kafkaTemplate;

    public void sendTo(String topic, MessageDto payload) {
        try {
            kafkaTemplate.send(topic, payload);
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
