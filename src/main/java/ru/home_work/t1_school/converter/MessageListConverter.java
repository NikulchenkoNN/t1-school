package ru.home_work.t1_school.converter;

import org.mapstruct.Mapper;
import ru.home_work.t1_school.model.Message;
import ru.home_work.t1_school.model.MessageDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = MessageConverter.class)
public interface MessageListConverter {
    List<Message> toMessageList(List<MessageDto> messages);
    List<MessageDto> toMessageDtoList(List<Message> messages);
}
