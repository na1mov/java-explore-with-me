package ru.practicum.explore.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.CommentDto;
import ru.practicum.explore.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto);

    List<CommentDto> findByAuthorId(Long userId, Pageable pageable);

    void delete(Long userId, Long commentId);

    List<CommentDto> findByEventId(Long eventId, Pageable pageable);

    void deleteForAdmin(Long commentId);
}
