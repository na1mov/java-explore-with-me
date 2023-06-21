package ru.practicum.explore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.CommentDto;
import ru.practicum.explore.dto.NewCommentDto;
import ru.practicum.explore.exception.MyForbiddenException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.model.Comment;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.User;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = findUserById(userId);
        Event event = findEventById(eventId);

        Comment comment = Comment.builder()
                .text(newCommentDto.getText())
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();

        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {
        checkUserById(userId);
        Comment commentForUpdate = findById(commentId);
        if (!Objects.equals(commentForUpdate.getAuthor().getId(), userId)) {
            throw new MyForbiddenException("Ошибка доступа. Пользователь не является автором комментария");
        }

        commentForUpdate.setText(newCommentDto.getText());
        return commentMapper.commentToCommentDto(commentRepository.save(commentForUpdate));
    }

    @Override
    public List<CommentDto> findByAuthorId(Long userId, Pageable pageable) {
        checkUserById(userId);
        return commentRepository.findAllByAuthorIdOrderByCreatedOnAsc(userId, pageable).stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long commentId) {
        checkUserById(userId);
        Comment commentForDelete = findById(commentId);
        if (!Objects.equals(commentForDelete.getAuthor().getId(), userId)) {
            throw new MyForbiddenException("Ошибка доступа. Пользователь не является автором комментария");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findByEventId(Long eventId, Pageable pageable) {
        checkEventById(eventId);
        return commentRepository.findAllByEventIdOrderByCreatedOnAsc(eventId, pageable).stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteForAdmin(Long commentId) {
        checkCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Комментария с ID:%d нет в базе", commentId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("События с ID:%d нет в базе", eventId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователя с ID:%d нет в базе", userId)));
    }

    private void checkUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователя с ID:%d нет в базе", userId));
        }
    }

    private void checkEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("События с ID:%d нет в базе", eventId));
        }
    }

    private void checkCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Комментария с ID:%d нет в базе", commentId));
        }
    }
}
