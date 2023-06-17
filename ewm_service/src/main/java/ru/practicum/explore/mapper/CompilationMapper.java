package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.model.Compilation;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class, EventMapper.class, LocationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {
    CompilationDto compilationToCompilationDto(Compilation compilation);
}
