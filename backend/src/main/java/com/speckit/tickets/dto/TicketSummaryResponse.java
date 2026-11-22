package com.speckit.tickets.dto;

import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.domain.TicketStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketSummaryResponse {

    private final Long ticketId;
    private final String title;
    private final TicketStatus status;
    private final TicketPriority priority;
    private final String assignee;
    private final Instant createdAt;
}
