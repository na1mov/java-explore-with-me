package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStatsDto viewStatsToViewStatsDto(ViewStats viewStats);

    ViewStats viewStatsDtoToViewStats(ViewStatsDto viewStatsDto);
}
