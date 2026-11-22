package com.speckit.tickets.dto;

import com.speckit.tickets.domain.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private TicketStatus status;
}
