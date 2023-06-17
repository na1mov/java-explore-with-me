package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;
import ru.practicum.explore.model.Category;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryDto categoryToCategoryDto(Category category);

    Category categoryDtoToCategory(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void updateCategory(@MappingTarget Category category, NewCategoryDto newCategoryDto);
}
