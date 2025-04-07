package ru.home_work.t1_school.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.model.MessageDto;
import ru.home_work.t1_school.service.MailNotificator;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final MailNotificator notificator;

    @KafkaListener(id = "${kafka.consumer.groupId}",
            topics = "${kafka.clientTopic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(@Payload List<MessageDto> messages,
                        Acknowledgment ack) {
        try {
            for (MessageDto messageDto : messages) {
                System.out.println(messageDto);
            }
            notificator.sendNotification();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();
        }
    }

}
