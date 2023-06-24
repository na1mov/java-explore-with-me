package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CommentDto;
import ru.practicum.explore.dto.NewCommentDto;
import ru.practicum.explore.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("{userId}/comments/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto save(@PathVariable Long userId,
                           @PathVariable Long eventId,
                           @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.save(userId, eventId, newCommentDto);
    }

    @PatchMapping("{userId}/comments/{commentId}")
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long commentId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.update(userId, commentId, newCommentDto);
    }

    @DeleteMapping("{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.delete(userId, commentId);
    }

    @GetMapping("{userId}/comments")
    public List<CommentDto> findByAuthorId(@PathVariable Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        return commentService.findByAuthorId(userId, PageRequest.of(from / size, size));
    }
}
