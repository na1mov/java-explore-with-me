package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;
import ru.practicum.explore.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PatchMapping("{catId}")
    public CategoryDto update(@PathVariable("catId") Long catId,
                              @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.update(catId, newCategoryDto);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable("catId") Long catId) {
        categoryService.delete(catId);
    }
}
