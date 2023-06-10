package ru.practicum.explore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void delete(Long compId);

    CompilationDto findCompilationDtoById(Long compId);

    List<CompilationDto> findAll(Boolean pinned, Pageable pageable);
}
