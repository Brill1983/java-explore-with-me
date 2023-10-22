package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("В метод postCategory переданы данные: newCategoryDto = {}", newCategoryDto);
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("В метод deleteCategory переданы данные: categoryId = {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable long catId,
                                     @RequestBody @Valid CategoryDto categoryDto) {
        log.info("В метод patchCategory переданы данные: categoryId = {}, categoryDto = {}", catId, categoryDto);
        return categoryService.patchCategory(catId, categoryDto);
    }

}
