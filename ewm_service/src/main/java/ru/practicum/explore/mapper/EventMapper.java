package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.dto.EventDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.model.Event;

@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    EventDto eventToEventDto(Event event);

    EventShortDto eventToEventShortDto(Event event);

    EventFullDto eventToEventFullDto(Event event);
}
