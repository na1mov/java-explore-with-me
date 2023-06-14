package ru.practicum.explore.service;

import ru.practicum.explore.dto.EventDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveEndpointHit(HttpServletRequest request);

    void setViewForEventDto(List<EventDto> eventDtoList);

    void setViewForEventFullDto(List<EventFullDto> eventFullDtoList);

    void setViewForEventShortDto(List<EventShortDto> eventShortDtoList, LocalDateTime start);
}
