package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compId);

    CompilationDto patchCompilation(long compId, UpdateCompilationRequest compilationDto);

    List<CompilationDto> getPublicCompList(Boolean pinned, int from, int size);

    CompilationDto getPublicCompById(long compId);
}
