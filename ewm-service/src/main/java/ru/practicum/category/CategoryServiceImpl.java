package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.exceptions.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category =  categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + categoryId + " не найдена"));
        //TODO проверка на связи - нужна ли или можно на уровне ДБ выбросить исключение?
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + catId + " не найдена"));
        Category categoryFromDto = CategoryMapper.toCategory(categoryDto, category);
        categoryFromDto.setId(catId);
        return CategoryMapper.toCategoryDto(categoryRepository.save(categoryFromDto));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);

        return categoryRepository.findAll(page).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + catId + " не найдена"));
        return CategoryMapper.toCategoryDto(category);
    }
}
