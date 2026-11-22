package com.speckit.tickets.service;

import com.speckit.tickets.domain.Ticket;
import com.speckit.tickets.domain.TicketComment;
import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.domain.TicketStatus;
import com.speckit.tickets.dto.CommentResponse;
import com.speckit.tickets.dto.CreateCommentRequest;
import com.speckit.tickets.dto.CreateTicketRequest;
import com.speckit.tickets.dto.PageResponse;
import com.speckit.tickets.dto.TicketDetailResponse;
import com.speckit.tickets.dto.TicketSummaryResponse;
import com.speckit.tickets.dto.UpdateStatusRequest;
import com.speckit.tickets.dto.UpdateTicketRequest;
import com.speckit.tickets.exception.InvalidStatusTransitionException;
import com.speckit.tickets.exception.TicketNotFoundException;
import com.speckit.tickets.repository.TicketCommentRepository;
import com.speckit.tickets.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCommentRepository commentRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public TicketDetailResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority() != null ? request.getPriority() : TicketPriority.MEDIUM);
        ticket.setAssignee(request.getAssignee());
        ticket.setStatus(TicketStatus.OPEN);

        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toDetail(saved, List.of());
    }

    @Transactional(readOnly = true)
    public PageResponse<TicketSummaryResponse> listTickets(TicketStatus status, String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Ticket> result = ticketRepository.search(status, keyword, pageable);

        return PageResponse.<TicketSummaryResponse>builder()
                .content(result.getContent().stream().map(ticketMapper::toSummary).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public TicketDetailResponse getTicket(Long id) {
        Ticket ticket = findTicket(id);
        var comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(id);
        return ticketMapper.toDetail(ticket, comments);
    }

    @Transactional
    public TicketDetailResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = findTicket(id);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setAssignee(request.getAssignee());

        Ticket saved = ticketRepository.save(ticket);
        var comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(id);
        return ticketMapper.toDetail(saved, comments);
    }

    @Transactional
    public TicketDetailResponse updateStatus(Long id, UpdateStatusRequest request) {
        Ticket ticket = findTicket(id);
        TicketStatus current = ticket.getStatus();
        TicketStatus target = request.getStatus();

        if (!current.canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(current, target);
        }

        if (current != target) {
            ticket.setStatus(target);
            ticket = ticketRepository.save(ticket);
        }

        var comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(id);
        return ticketMapper.toDetail(ticket, comments);
    }

    @Transactional
    public CommentResponse addComment(Long id, CreateCommentRequest request) {
        Ticket ticket = findTicket(id);

        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setBody(request.getBody());
        if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
            comment.setAuthor(request.getAuthor());
        }

        TicketComment saved = commentRepository.save(comment);
        return ticketMapper.toComment(saved);
    }

    private Ticket findTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}
