package com.speckit.tickets.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speckit.tickets.domain.TicketPriority;
import com.speckit.tickets.dto.CreateCommentRequest;
import com.speckit.tickets.dto.CreateTicketRequest;
import com.speckit.tickets.dto.UpdateTicketRequest;
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
class TicketApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTicketWithValidationErrors() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("ab");
        request.setDescription("");
        request.setPriority(null);

        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[?(@.field=='title')]").exists());
    }

    @Test
    void searchAndFilterTickets() throws Exception {
        createTicket("Login page error", "Users cannot log in", TicketPriority.HIGH);
        createTicket("Billing export slow", "CSV export takes too long", TicketPriority.MEDIUM);

        mockMvc.perform(get("/api/v1/tickets")
                        .param("keyword", "login")
                        .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Login page error"));

        mockMvc.perform(get("/api/v1/tickets").param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void updateTicketAndAddComment() throws Exception {
        Long ticketId = createTicket("Printer offline", "Office printer not responding", TicketPriority.LOW);

        UpdateTicketRequest update = new UpdateTicketRequest();
        update.setTitle("Printer offline - floor 3");
        update.setDescription("Office printer not responding since 09:00");
        update.setPriority(TicketPriority.HIGH);
        update.setAssignee("Alice");

        mockMvc.perform(put("/api/v1/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignee").value("Alice"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        CreateCommentRequest comment = new CreateCommentRequest();
        comment.setBody("Rebooted the printer");
        comment.setAuthor("Bob");

        mockMvc.perform(post("/api/v1/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value("Rebooted the printer"));

        mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].author").value("Bob"));
    }

    private Long createTicket(String title, String description, TicketPriority priority) throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setPriority(priority);

        MvcResult result = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("ticketId").asLong();
    }
}
