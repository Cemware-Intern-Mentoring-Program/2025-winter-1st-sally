package com.cemware.sally.dto.task;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        Long groupId,
        String title,
        String description,
        String status,
        LocalDate dueDate
) { }