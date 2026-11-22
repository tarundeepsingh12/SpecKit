package com.speckit.tickets.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private final Long id;
    private final String body;
    private final String author;
    private final Instant createdAt;
}
