package ru.home_work.t1_school.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.converter.MessageConverter;
import ru.home_work.t1_school.model.Message;
import ru.home_work.t1_school.model.MessageDto;
import ru.home_work.t1_school.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final NotificationService notificator;
    private final MessageConverter messageConverter;

    @KafkaListener(id = "${kafka.consumer.groupId}",
            topics = "${kafka.clientTopic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(@Payload List<MessageDto> messages,
                        Acknowledgment ack) {
        try {
            log.info("Consuming messages: {}", messages);
            List<Message> messageList = new ArrayList<>();
            for (MessageDto messageDto : messages) {
                Message message = messageConverter.toMessage(messageDto);
                messageList.add(message);
            }
            log.info("Consumed messages: {}", messageList);
            for (Message message : messageList) {
                notificator.sendNotification(message.getId());
            }
            log.info("Message sent: {}", messageList.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
    }

}
