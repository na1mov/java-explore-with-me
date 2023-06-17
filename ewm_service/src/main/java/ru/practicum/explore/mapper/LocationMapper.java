package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.dto.LocationDto;
import ru.practicum.explore.model.Location;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocationMapper {
    Location locationDtoToLocation(LocationDto locationDto);

    LocationDto locationToLocationDto(Location location);
}
