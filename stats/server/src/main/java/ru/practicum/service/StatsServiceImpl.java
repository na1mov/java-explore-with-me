package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.MyDataException;
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
        log.info(String.format("Сохранение статистики пользователя c IP:%s", endpointHitDto.getIp()));
        return endpointHitMapper.endpointHitToEndpointDto(
                statsRepository.save(endpointHitMapper.endpointHitDtoToEndpoint(endpointHitDto)));
    }

    @Override
    public List<ViewStatsDto> find(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Запрос на получение статистики");

        if (start.isAfter(end)) {
            throw new MyDataException(String.format("Ошибка времени start:%s end:%s", start, end));
        }
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.findUniqueViewStats(start, end).stream()
                        .map(viewStatsMapper::viewStatsToViewStatsDto)
                        .collect(Collectors.toList());
            }
            return statsRepository.findUniqueViewStats(start, end, uris).stream()
                    .map(viewStatsMapper::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.findViewStats(start, end).stream()
                        .map(viewStatsMapper::viewStatsToViewStatsDto)
                        .collect(Collectors.toList());
            }
            return statsRepository.findViewStats(start, end, uris).stream()
                    .map(viewStatsMapper::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        }
    }
}
