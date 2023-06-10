package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.service.EventService;
import ru.practicum.explore.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto save(@PathVariable("userId") Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.save(userId, newEventDto);
    }

    @PatchMapping("{userId}/events/{eventId}")
    public EventFullDto updateForUser(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateForUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventDto findByIdAndUser(@PathVariable("userId") Long userId, @PathVariable Long eventId) {
        return eventService.findByIdAndUser(userId, eventId);
    }

    @GetMapping("{userId}/events")
    public List<EventShortDto> findAllForUser(@PathVariable("userId") Long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size) {
        return eventService.findAllForUser(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public List<RequestDto> findAllRequestsByEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.findAllRequestsByEventId(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest requestStatusUpdateDto) {
        return requestService.updateRequestStatus(userId, eventId, requestStatusUpdateDto);
    }
}
