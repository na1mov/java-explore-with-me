package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN ?3 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<ViewStats> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<ViewStats> findUniqueViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN ?3 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end);
}
