package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.dto.RequestDto;
import ru.practicum.explore.model.Request;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class, EventMapper.class, LocationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {
    @Mapping(source = "requester.id", target = "requester")
    @Mapping(source = "event.id", target = "event")
    RequestDto requestToRequestDto(Request request);
}
