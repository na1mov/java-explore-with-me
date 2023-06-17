package ru.practicum.explore.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Set<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
