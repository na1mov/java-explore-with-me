package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.dto.UpdateCompilationRequest;
import ru.practicum.explore.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.save(newCompilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto update(@Positive @PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.update(compId, updateCompilationRequest);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable Long compId) {
        compilationService.delete(compId);
    }
}
