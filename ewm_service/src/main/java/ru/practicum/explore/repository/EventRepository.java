package ru.practicum.explore.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Page<Event> findAll(Predicate predicate, Pageable pageable);

    Set<Event> findAllByIdIn(Set<Long> events);
}
