package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return new Compilation(
                null,
                compilationDto.getTitle(),
                compilationDto.isPinned(),
                new HashSet<>()
        );
    }

    public CompilationDto toDto(Compilation compilation, List<EventShortDto> eventsList) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                new HashSet<>(eventsList)
        );
    }

    public CompilationDto toDto(Compilation compilation, Set<EventShortDto> eventsList) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                eventsList
        );
    }

    public Compilation toCompFromUpdateDto(UpdateCompilationRequest dto, Compilation comp) {
        return new Compilation(
                comp.getId(),
                dto.getTitle() != null ? dto.getTitle() : comp.getTitle(),
                dto.getPinned() != null ? dto.getPinned() : comp.getPinned(),
                dto.getEvents() != null ? new HashSet<>() : comp.getEvents()
        );
    }
}
