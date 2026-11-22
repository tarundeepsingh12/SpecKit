package com.speckit.tickets.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TicketStatusTest {

    @Test
    void allowsValidTransitions() {
        assertTrue(TicketStatus.OPEN.canTransitionTo(TicketStatus.IN_PROGRESS));
        assertTrue(TicketStatus.OPEN.canTransitionTo(TicketStatus.CANCELLED));
        assertTrue(TicketStatus.IN_PROGRESS.canTransitionTo(TicketStatus.RESOLVED));
        assertTrue(TicketStatus.IN_PROGRESS.canTransitionTo(TicketStatus.CANCELLED));
        assertTrue(TicketStatus.RESOLVED.canTransitionTo(TicketStatus.CLOSED));
    }

    @Test
    void rejectsInvalidTransitions() {
        assertFalse(TicketStatus.CLOSED.canTransitionTo(TicketStatus.OPEN));
        assertFalse(TicketStatus.RESOLVED.canTransitionTo(TicketStatus.OPEN));
        assertFalse(TicketStatus.CANCELLED.canTransitionTo(TicketStatus.OPEN));
        assertFalse(TicketStatus.OPEN.canTransitionTo(TicketStatus.CLOSED));
        assertFalse(TicketStatus.OPEN.canTransitionTo(TicketStatus.RESOLVED));
    }

    @Test
    void terminalStatusesHaveNoOutgoingTransitions() {
        assertTrue(TicketStatus.CLOSED.isTerminal());
        assertTrue(TicketStatus.CANCELLED.isTerminal());
        assertFalse(TicketStatus.CLOSED.canTransitionTo(TicketStatus.CANCELLED));
        assertFalse(TicketStatus.CANCELLED.canTransitionTo(TicketStatus.CLOSED));
    }
}
