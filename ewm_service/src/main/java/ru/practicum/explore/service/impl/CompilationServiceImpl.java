package ru.practicum.explore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.dto.UpdateCompilationRequest;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        }

        Set<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::eventToEventShortDto)
                .collect(Collectors.toSet());

        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
        compilation = compilationRepository.save(compilation);

        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventShortDtoList)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = findById(compId);
        if (updateCompilationRequest.getTitle() != null && !(updateCompilationRequest.getTitle().isBlank())) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(Long compId) {
        findById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto findCompilationDtoById(Long compId) {
        Compilation compilation = findById(compId);
        return compilationMapper.compilationToCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        return compilations.stream()
                .map(compilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    private Compilation findById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборки событий с ID:%d нет в базе", compId)));
    }
}
