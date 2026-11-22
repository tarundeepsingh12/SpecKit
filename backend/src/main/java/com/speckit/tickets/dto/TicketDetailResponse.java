package com.speckit.tickets.dto;

import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.domain.TicketStatus;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketDetailResponse {

    private final Long ticketId;
    private final String title;
    private final String description;
    private final TicketStatus status;
    private final TicketPriority priority;
    private final String assignee;
    private final Long version;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<CommentResponse> comments;
}
