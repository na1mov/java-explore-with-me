package ru.practicum.explore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private long participantLimit;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120)
    @NotBlank
    private String title;
}
