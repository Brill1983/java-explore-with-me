package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);
}
