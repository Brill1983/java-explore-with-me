package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("В метод postCategory переданы данные: newCategoryDto = {}", newCategoryDto);
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping
    public void deleteCategory(long categoryId) {

    }

}
