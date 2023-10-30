package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ElementNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

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

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new ElementNotFoundException("Подборки с ID: " + compId + " не найдено"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest compilationDto) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ElementNotFoundException("Подборки с ID: " + compId + " не найдено"));

        Compilation newComp = CompilationMapper.toCompFromUpdateDto(compilationDto, compilation);

        List<Event> eventList;
        List<EventShortDto> eventShortDtoList;

        if (compilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(compilationDto.getEvents());
            compilation.setEvents(new HashSet<>(eventList));

            eventShortDtoList = eventService.mapEventsToShortDtos(eventList);
        } else {
            eventShortDtoList = eventService.mapEventsToShortDtos(new ArrayList<>(compilation.getEvents()));
        }

        Compilation compilationFromDb = compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilationFromDb, eventShortDtoList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getPublicCompList(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);

        List<Compilation> compilationList;
        if (pinned != null) {
            compilationList = compilationRepository.findAllByPinned(pinned, page).toList();
        } else {
            compilationList = compilationRepository.findAll(page).toList();
        }

        Map<Compilation, Set<Long>> compMap = new HashMap<>();
        Set<Event> eventSet = new HashSet<>();
        for (Compilation compilation : compilationList) {
            compMap.put(compilation, compilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet()));
            eventSet.addAll(compilation.getEvents());
        }

        List<EventShortDto> eventShortDtoList = eventService.mapEventsToShortDtos(new ArrayList<>(eventSet));

        List<CompilationDto> responseDtoList = new ArrayList<>();
        for (Compilation compilation : compMap.keySet()) {
            Set<EventShortDto> eventDtosForComp = new HashSet<>();
            for (EventShortDto eventShortDto : eventShortDtoList) {
                if (compMap.get(compilation).contains(eventShortDto.getId())) {
                    eventDtosForComp.add(eventShortDto);
                }
            }
            CompilationDto compilationDto  = CompilationMapper.toDto(compilation, eventDtosForComp);
            responseDtoList.add(compilationDto);
        }

        return responseDtoList.stream()
                .sorted(Comparator.comparingLong(CompilationDto::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getPublicCompById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ElementNotFoundException("Подборки с ID: " + compId + " не найдено"));

        List<EventShortDto> eventShortDtoList = eventService.mapEventsToShortDtos(new ArrayList<>(compilation.getEvents()));

        return CompilationMapper.toDto(compilation, eventShortDtoList);
    }

}
