package ru.practicum.explore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.model.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto save(Long userId, NewEventDto newEventDto);

    EventFullDto updateForUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventDto findByIdAndUser(Long userId, Long eventId);

    List<EventShortDto> findAllForUser(Long userId, Pageable pageable);

    EventFullDto updateForAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> findAllForAdmin(List<Long> usersIds, List<EventState> states, List<Long> categoriesIds,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventDto findEventDtoById(Long id, HttpServletRequest request);

    List<EventFullDto> findAllWithParams(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Pageable pageable, HttpServletRequest request);
}
