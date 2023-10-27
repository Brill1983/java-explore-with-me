package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventServiceImpl;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventServiceImpl eventService;

    @Transactional
    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {

        Compilation compilation = CompilationMapper.toCompilation(compilationDto);

        List<Event> eventList;
        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        if (compilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(compilationDto.getEvents());
            compilation.setEvents(new HashSet<>(eventList));

            eventShortDtoList = eventService.mapEventsToShortDtos(eventList);
        }
        Compilation compilationFromDb = compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilationFromDb, eventShortDtoList);
    }
}
