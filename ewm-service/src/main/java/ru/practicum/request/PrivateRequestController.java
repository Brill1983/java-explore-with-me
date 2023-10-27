package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUsersParticipantsRequests(@PathVariable long userId) {

        log.info("В метод getUsersParticipantsRequests переданы данные: userId = {}", userId);
        return requestService.getUsersRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto postParticipantRequest(@PathVariable long userId,
                                                          @RequestParam long eventId) {
        log.info("В метод getUsersParticipantsRequests переданы данные: userId = {}, eventId = {}", userId, eventId);
        return requestService.postRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchRequestCancel(@PathVariable long userId,
                                                      @RequestParam long requestId) {
        log.info("В метод patchRequestCancel переданы данные: userId = {}, requestId = {}", userId, requestId);
        return requestService.patchRequest(userId, requestId);
    }

}
