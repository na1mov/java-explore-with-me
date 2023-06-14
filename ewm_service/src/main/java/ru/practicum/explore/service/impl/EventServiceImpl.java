package ru.practicum.explore.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exception.MyConflictException;
import ru.practicum.explore.exception.MyValidationException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.mapper.LocationMapper;
import ru.practicum.explore.model.*;
import ru.practicum.explore.model.enums.EventState;
import ru.practicum.explore.model.enums.RequestStatus;
import ru.practicum.explore.model.enums.StateAction;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.EventService;
import ru.practicum.explore.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatsService statsService;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    @Override
    public EventDto save(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new MyValidationException(String.format("Ошибка времени: %s", newEventDto.getEventDate()));
        }

        User initiator = findUserById(userId);
        Category category = findCategoryById(newEventDto.getCategory());


        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(locationMapper.locationDtoToLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();

        return eventMapper.eventToEventDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateForUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (updateEventUserRequest.getEventDate() != null
                && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new MyValidationException(
                    String.format("Ошибка времени: %s", updateEventUserRequest.getEventDate()));
        }

        findUserById(userId);
        Event eventForUpdate = findById(eventId);

        if (eventForUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new MyConflictException(String.format("Ошибка статуса: %s", eventForUpdate.getState()));
        }

        if (updateEventUserRequest.getEventDate() != null) {
            eventForUpdate.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getAnnotation() != null && !(updateEventUserRequest.getAnnotation().isBlank())) {
            eventForUpdate.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getDescription() != null && !(updateEventUserRequest.getDescription().isBlank())) {
            eventForUpdate.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getLocation() != null) {
            Location location = eventForUpdate.getLocation();
            location.setLon(updateEventUserRequest.getLocation().getLon());
            location.setLat(updateEventUserRequest.getLocation().getLat());
        }
        if (updateEventUserRequest.getPaid() != null) {
            eventForUpdate.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            eventForUpdate.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            eventForUpdate.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null && !(updateEventUserRequest.getTitle().isBlank())) {
            eventForUpdate.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = findCategoryById(updateEventUserRequest.getCategory());
            eventForUpdate.setCategory(category);
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                eventForUpdate.setState(EventState.CANCELED);
            }
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                eventForUpdate.setState(EventState.PENDING);
            }
        }

        return eventMapper.eventToEventFullDto(eventRepository.save(eventForUpdate));
    }

    @Override
    public EventDto findByIdAndUser(Long userId, Long eventId) {
        Event event = findById(eventId);
        findUserById(userId);
        EventDto eventDto = eventMapper.eventToEventDto(event);
        statsService.setViewForEventDto(List.of(eventDto));
        return eventDto;
    }

    @Override
    public List<EventShortDto> findAllForUser(Long userId, Pageable pageable) {
        findUserById(userId);
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId, pageable);
        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(eventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
        statsService.setViewForEventShortDto(eventShortDtoList);
        return eventShortDtoList;
    }

    @Override
    public EventFullDto updateForAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null
                && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new MyValidationException(
                    String.format("Ошибка времени: %s", updateEventAdminRequest.getEventDate()));
        }

        Event eventForUpdate = findById(eventId);

        if (!eventForUpdate.getState().equals(EventState.PENDING)) {
            throw new MyConflictException(String.format("Ошибка статуса: %s", eventForUpdate.getState()));
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            eventForUpdate.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getTitle() != null && !(updateEventAdminRequest.getTitle().isBlank())) {
            eventForUpdate.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getAnnotation() != null && !(updateEventAdminRequest.getAnnotation().isBlank())) {
            eventForUpdate.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null && !(updateEventAdminRequest.getDescription().isBlank())) {
            eventForUpdate.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            eventForUpdate.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            eventForUpdate.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            eventForUpdate.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            eventForUpdate.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = findCategoryById(updateEventAdminRequest.getCategory());
            eventForUpdate.setCategory(category);
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                eventForUpdate.setState(EventState.PUBLISHED);
                eventForUpdate.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                eventForUpdate.setState(EventState.CANCELED);
            }
        }
        return eventMapper.eventToEventFullDto(eventRepository.save(eventForUpdate));
    }

    @Override
    public List<EventFullDto> findAllForAdmin(List<Long> usersIds, List<EventState> states, List<Long> categoriesIds,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QEvent qEvent = QEvent.event;
        if (usersIds != null) {
            booleanBuilder.and(QEvent.event.initiator.id.in(usersIds));
        }
        if (states != null) {
            booleanBuilder.and(QEvent.event.state.in(states));
        }
        if (categoriesIds != null) {
            booleanBuilder.and(QEvent.event.category.id.in(categoriesIds));
        }
        if (rangeStart != null) {
            booleanBuilder.and(qEvent.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            booleanBuilder.and(qEvent.eventDate.before(rangeEnd));
        }
        if (rangeStart == null) {
            booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.now()));
        }

        List<Event> eventList = eventRepository
                .findAll(Objects.requireNonNull(booleanBuilder.getValue()), pageable).getContent();
        List<EventFullDto> eventFullDtoList = eventList.stream()
                .map(eventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
        statsService.setViewForEventFullDto(eventFullDtoList);
        setConfirmedRequestsForEventFullDto(eventFullDtoList);

        return eventFullDtoList;
    }

    @Override
    public EventDto findEventDtoById(Long eventId, HttpServletRequest request) {
        Event event = findById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(String.format("События с ID:%d нет среди опубликованных", eventId));
        }

        statsService.saveEndpointHit(request);
        EventDto eventDto = eventMapper.eventToEventDto(event);
        statsService.setViewForEventDto(List.of(eventDto));
        setConfirmedRequestsForEventDto(List.of(eventDto));
        return eventDto;
    }

    @Override
    public List<EventFullDto> findAllWithParams(String text, List<Long> categories,
                                                Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Pageable pageable,
                                                HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd) || rangeEnd.isBefore(LocalDateTime.now())) {
                throw new MyValidationException(
                        String.format("Ошибка времени: %s", rangeEnd));
            }
        }

        statsService.saveEndpointHit(request);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QEvent qEvent = QEvent.event;
        booleanBuilder.and(qEvent.state.eq(EventState.PUBLISHED));
        if (text != null) {
            booleanBuilder.and(qEvent.annotation.containsIgnoreCase(text)
                    .or(qEvent.description.containsIgnoreCase(text)));
        }
        if (categories != null) {
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        }
        if (rangeStart == null) {
            booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.now()));
        }
        if (rangeStart != null) {
            booleanBuilder.and(qEvent.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            booleanBuilder.and(qEvent.eventDate.before(rangeEnd));
        }

        List<Event> eventList = eventRepository
                .findAll(Objects.requireNonNull(booleanBuilder.getValue()), pageable).getContent();
        List<EventFullDto> eventFullDtoList = eventList.stream()
                .map(eventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
        statsService.setViewForEventFullDto(eventFullDtoList);

        return eventFullDtoList;
    }

    private Event findById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("События с ID:%d нет в базе", eventId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("События с ID:%d нет в базе", userId)));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Категории с ID:%d нет в базе", categoryId)));
    }

    private void setConfirmedRequestsForEventFullDto(List<EventFullDto> eventFullDtoList) {
        List<Long> eventIds = eventFullDtoList.stream().map(EventFullDto::getId).collect(Collectors.toList());
        List<Request> requestList = requestRepository.findAllByEventIdInAndStatus(eventIds, RequestStatus.CONFIRMED);
        Map<Long, Long> eventIdToConfirmedCount = new HashMap<>();

        for (Request request : requestList) {
            Long eventId = request.getEvent().getId();
            if (!eventIdToConfirmedCount.containsKey(eventId)) {
                eventIdToConfirmedCount.put(eventId, 1L);
            } else {
                eventIdToConfirmedCount.put(eventId, eventIdToConfirmedCount.get(eventId) + 1);
            }
        }

        for (EventFullDto eventFullDto : eventFullDtoList) {
            eventFullDto.setConfirmedRequests(eventIdToConfirmedCount.getOrDefault(eventFullDto.getId(), 0L));
        }
    }

    private void setConfirmedRequestsForEventDto(List<EventDto> eventDtoList) {
        List<Long> eventIds = eventDtoList.stream().map(EventDto::getId).collect(Collectors.toList());
        List<Request> requestList = requestRepository.findAllByEventIdInAndStatus(eventIds, RequestStatus.CONFIRMED);
        Map<Long, Long> eventIdToConfirmedCount = new HashMap<>();

        for (Request request : requestList) {
            Long eventId = request.getEvent().getId();
            if (!eventIdToConfirmedCount.containsKey(eventId)) {
                eventIdToConfirmedCount.put(eventId, 1L);
            } else {
                eventIdToConfirmedCount.put(eventId, eventIdToConfirmedCount.get(eventId) + 1);
            }
        }

        for (EventDto eventDto : eventDtoList) {
            eventDto.setConfirmedRequests(eventIdToConfirmedCount.getOrDefault(eventDto.getId(), 0L));
        }
    }
}
