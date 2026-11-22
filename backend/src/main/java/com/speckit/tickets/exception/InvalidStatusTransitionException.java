package com.speckit.tickets.exception;

import com.speckit.tickets.domain.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus from, TicketStatus to) {
        super(String.format("Invalid status transition from %s to %s", from, to));
    }
}
