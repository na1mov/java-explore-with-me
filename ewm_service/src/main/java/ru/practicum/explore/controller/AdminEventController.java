package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.UpdateEventAdminRequest;
import ru.practicum.explore.model.enums.EventState;
import ru.practicum.explore.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("{eventId}")
    public EventFullDto updateForAdmin(@PathVariable Long eventId,
                                       @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateForAdmin(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDto> findAllForAdmin(@RequestParam(required = false) List<Long> usersIds,
                                              @RequestParam(required = false) List<EventState> states,
                                              @RequestParam(required = false) List<Long> categoriesIds,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        return eventService.findAllForAdmin(usersIds, states, categoriesIds, rangeStart, rangeEnd,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id")));
    }
}
