package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final ViewStatsMapper viewStatsMapper;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        return endpointHitMapper.endpointHitToEndpointDto(
                statsRepository.save(endpointHitMapper.endpointHitDtoToEndpoint(endpointHitDto)));
    }

    @Override
    public List<ViewStatsDto> find(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return statsRepository.findUniqueViewStats(start, end, uris).stream()
                    .map(viewStatsMapper::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            return statsRepository.findViewStats(start, end, uris).stream()
                    .map(viewStatsMapper::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        }
    }
}
