package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EventDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.model.enums.FilterSort;
import ru.practicum.explore.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("{id}")
    public EventDto findById(@Positive @PathVariable Long id, HttpServletRequest request) {
        return eventService.findEventDtoById(id, request);
    }

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(required = false, defaultValue = "EVENT_DATE") FilterSort sort,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        Pageable pageable;
        if (sort.equals(FilterSort.EVENT_DATE)) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        } else {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "views"));
        }
        return eventService.findAllWithParams(text, categories, paid, rangeStart, rangeEnd, pageable, request);
    }
}
