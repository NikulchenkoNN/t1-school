package ru.home_work.t1_school.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;
import ru.home_work.t1_school.converter.MessageConverter;
import ru.home_work.t1_school.exception.MessageSendException;
import ru.home_work.t1_school.model.MessageDto;
import ru.home_work.t1_school.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class KafkaMessageConsumerTest {
    private final NotificationService notificator = mock(NotificationService.class);
    private final Acknowledgment ack = mock(Acknowledgment.class);
    private final MessageConverter messageConverter = MessageConverter.INSTANCE;
    private KafkaMessageConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new KafkaMessageConsumer(notificator, messageConverter);
    }

    @Test
    void sendMessageOk() {
        int count = 5;
        List<MessageDto> messages = createMessages(count);
        consumer.consume(messages, ack);

        verify(notificator, times(count)).sendNotification(anyLong());
        verify(ack).acknowledge();
    }

    @Test
    void sendMessageThrowsException() {
        int count = 5;
        List<MessageDto> messages = createMessages(count);
        doThrow(new MessageSendException()).when(notificator).sendNotification(anyLong());

        assertThrows(MessageSendException.class, () -> consumer.consume(messages, ack));

        verify(notificator, times(1)).sendNotification(anyLong());
        verify(ack).acknowledge();
    }

    private List<MessageDto> createMessages(int count) {
        ArrayList<MessageDto> messages = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            messages.add(new MessageDto(i, "state"));
        }
        return messages;
    }
}