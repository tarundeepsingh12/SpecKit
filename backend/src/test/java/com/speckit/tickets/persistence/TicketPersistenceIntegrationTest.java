package com.speckit.tickets.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.speckit.tickets.domain.Ticket;
import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.domain.TicketStatus;
import com.speckit.tickets.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class TicketPersistenceIntegrationTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @Transactional
    void ticketPersistsWithAllFields() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Persistence test ticket");
        ticket.setDescription("Verifies data survives save and reload");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setAssignee("Alice");

        Ticket saved = ticketRepository.save(ticket);
        ticketRepository.flush();

        Ticket loaded = ticketRepository.findById(saved.getId()).orElseThrow();

        assertThat(loaded.getTitle()).isEqualTo("Persistence test ticket");
        assertThat(loaded.getStatus()).isEqualTo(TicketStatus.OPEN);
        assertThat(loaded.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(loaded.getAssignee()).isEqualTo("Alice");
        assertThat(loaded.getCreatedAt()).isNotNull();
        assertThat(loaded.getUpdatedAt()).isNotNull();
        assertThat(loaded.getVersion()).isNotNull();
    }
}
