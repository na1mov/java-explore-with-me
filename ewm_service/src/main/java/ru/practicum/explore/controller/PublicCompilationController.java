package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("{compId}")
    public CompilationDto findCompilationDtoById(@PositiveOrZero @PathVariable Long compId) {
        return compilationService.findCompilationDtoById(compId);
    }

    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.findAll(pinned,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id")));
    }
}
