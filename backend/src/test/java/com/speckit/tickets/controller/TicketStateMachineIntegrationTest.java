package com.speckit.tickets.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.domain.TicketStatus;
import com.speckit.tickets.dto.CreateTicketRequest;
import com.speckit.tickets.dto.UpdateStatusRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TicketStateMachineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validLifecycleTransitionsSucceed() throws Exception {
        Long ticketId = createTicket();

        transition(ticketId, TicketStatus.IN_PROGRESS).andExpect(status().isOk());
        transition(ticketId, TicketStatus.RESOLVED).andExpect(status().isOk());
        transition(ticketId, TicketStatus.CLOSED).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void openCanBeCancelled() throws Exception {
        Long ticketId = createTicket();
        transition(ticketId, TicketStatus.CANCELLED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void inProgressCanBeCancelled() throws Exception {
        Long ticketId = createTicket();
        transition(ticketId, TicketStatus.IN_PROGRESS).andExpect(status().isOk());
        transition(ticketId, TicketStatus.CANCELLED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void closedCannotReopen() throws Exception {
        Long ticketId = createTicket();
        transition(ticketId, TicketStatus.IN_PROGRESS);
        transition(ticketId, TicketStatus.RESOLVED);
        transition(ticketId, TicketStatus.CLOSED);

        transition(ticketId, TicketStatus.OPEN)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Invalid status transition from CLOSED to OPEN"));
    }

    @Test
    void resolvedCannotReopen() throws Exception {
        Long ticketId = createTicket();
        transition(ticketId, TicketStatus.IN_PROGRESS);
        transition(ticketId, TicketStatus.RESOLVED);

        transition(ticketId, TicketStatus.OPEN)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Invalid status transition from RESOLVED to OPEN"));
    }

    @Test
    void cancelledCannotReopen() throws Exception {
        Long ticketId = createTicket();
        transition(ticketId, TicketStatus.CANCELLED);

        transition(ticketId, TicketStatus.OPEN)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Invalid status transition from CANCELLED to OPEN"));
    }

    @Test
    void openCannotSkipToClosed() throws Exception {
        Long ticketId = createTicket();

        transition(ticketId, TicketStatus.CLOSED)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Invalid status transition from OPEN to CLOSED"));
    }

    private Long createTicket() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("State machine test ticket");
        request.setDescription("Testing valid and invalid transitions");
        request.setPriority(TicketPriority.MEDIUM);

        MvcResult result = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("ticketId").asLong();
    }

    private org.springframework.test.web.servlet.ResultActions transition(Long ticketId, TicketStatus status)
            throws Exception {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(status);

        return mockMvc.perform(patch("/api/v1/tickets/{id}/status", ticketId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
}
