package ru.practicum.explore.dto;

import lombok.*;
import ru.practicum.explore.model.enums.RequestStatus;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    private List<Long> requestIds;
    @NotEmpty
    private RequestStatus status;
}
