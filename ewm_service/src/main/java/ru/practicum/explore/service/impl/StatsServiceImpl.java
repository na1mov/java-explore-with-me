package ru.practicum.explore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.explore.dto.EventDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {
    private final String serviceName;
    private final StatsClient statsClient;
    private final LocalDateTime start;

    @Autowired
    public StatsServiceImpl(@Value("${stats-server.url}") String url,
                            @Value("${ewm.service.name}") String serviceName) {
        this.serviceName = serviceName;
        this.statsClient = new StatsClient(url);
        this.start = LocalDateTime.now();
    }

    @Override
    public void saveEndpointHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.postEndpointHit(endpointHitDto);
    }

    @Override
    public void setViewForEventDto(List<EventDto> eventDtoList) {
        List<String> uris = new ArrayList<>();
        Map<String, EventDto> uriToEventDto = new HashMap<>();
        for (EventDto eventDto : eventDtoList) {
            String uri = "/events/" + eventDto.getId();
            uris.add(uri);
            uriToEventDto.put(uri, eventDto);
        }

        List<ViewStatsDto> viewStatsDtoList = statsClient.getViewStats(start, LocalDateTime.now(), uris, true);
        viewStatsDtoList.forEach((views) -> uriToEventDto.get(views.getUri()).setViews(views.getHits()));
    }

    @Override
    public void setViewForEventFullDto(List<EventFullDto> eventFullDtoList) {
        List<String> uris = new ArrayList<>();
        Map<String, EventFullDto> uriToEventFullDto = new HashMap<>();
        for (EventFullDto eventFullDto : eventFullDtoList) {
            String uri = "/events/" + eventFullDto.getId();
            uris.add(uri);
            uriToEventFullDto.put(uri, eventFullDto);
        }

        List<ViewStatsDto> viewStatsDtoList = statsClient.getViewStats(start, LocalDateTime.now(), uris, true);
        viewStatsDtoList.forEach((views) -> uriToEventFullDto.get(views.getUri()).setViews(views.getHits()));
    }

    @Override
    public void setViewForEventShortDto(List<EventShortDto> eventShortDtoList) {
        List<String> uris = new ArrayList<>();
        Map<String, EventShortDto> uriToEventShortDto = new HashMap<>();
        for (EventShortDto eventShortDto : eventShortDtoList) {
            String uri = "/events/" + eventShortDto.getId();
            uris.add(uri);
            uriToEventShortDto.put(uri, eventShortDto);
        }

        List<ViewStatsDto> viewStatsDtoList = statsClient.getViewStats(start, LocalDateTime.now(), uris, true);
        viewStatsDtoList.forEach((views) -> uriToEventShortDto.get(views.getUri()).setViews(views.getHits()));
    }
}
