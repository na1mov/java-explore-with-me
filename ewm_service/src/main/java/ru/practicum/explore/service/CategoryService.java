package ru.practicum.explore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);

    void delete(Long categoryId);

    CategoryDto findCategoryDtoById(Long categoryId);

    List<CategoryDto> findAll(Pageable pageable);
}
