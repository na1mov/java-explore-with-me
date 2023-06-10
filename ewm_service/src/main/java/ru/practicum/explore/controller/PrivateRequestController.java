package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.RequestDto;
import ru.practicum.explore.service.RequestService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto save(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.save(userId, eventId);
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public RequestDto update(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.update(userId, requestId);
    }

    @GetMapping("{userId}/requests")
    public List<RequestDto> findByUserId(@PathVariable Long userId) {
        return requestService.findByUserId(userId);
    }
}
