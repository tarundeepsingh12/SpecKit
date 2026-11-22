package com.speckit.tickets.service;

import com.speckit.tickets.domain.Ticket;
import com.speckit.tickets.domain.TicketComment;
import com.speckit.tickets.dto.CommentResponse;
import com.speckit.tickets.dto.TicketDetailResponse;
import com.speckit.tickets.dto.TicketSummaryResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketSummaryResponse toSummary(Ticket ticket) {
        return TicketSummaryResponse.builder()
                .ticketId(ticket.getId())
                .title(ticket.getTitle())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .assignee(ticket.getAssignee())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    public TicketDetailResponse toDetail(Ticket ticket, List<TicketComment> comments) {
        return TicketDetailResponse.builder()
                .ticketId(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .assignee(ticket.getAssignee())
                .version(ticket.getVersion())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .comments(comments.stream().map(this::toComment).toList())
                .build();
    }

    public CommentResponse toComment(TicketComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
