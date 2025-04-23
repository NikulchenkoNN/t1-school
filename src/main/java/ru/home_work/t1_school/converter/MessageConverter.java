package ru.home_work.t1_school.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.home_work.t1_school.model.Message;
import ru.home_work.t1_school.model.MessageDto;

@Mapper(componentModel = "spring")
public interface MessageConverter {
    MessageConverter INSTANCE = Mappers.getMapper(MessageConverter.class);

    MessageDto toDto(Message message);

    Message toMessage(MessageDto messageDto);
}
