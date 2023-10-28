package ru.practicum.compilation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto postCompilationAdmin(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("В метод postCompilationAdmin переданы данные: newUserDto = {}", compilationDto);
        return compilationService.saveCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("В метод deleteCompilation переданы данные: compId = {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable long compId,
                                           @RequestBody @Valid UpdateCompilationRequest compilationDto) {
        log.info("В метод patchCompilation переданы данные: compId = {}, compilationDto = {}", compId, compilationDto);
        return compilationService.patchCompilation(compId, compilationDto);
    }
}
