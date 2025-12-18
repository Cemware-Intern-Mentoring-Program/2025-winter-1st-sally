package com.cemware.sally.dto.task;

import java.time.LocalDate;

public record CreateTaskRequest(
        Long groupId,
        String title,
        String description,
        String status,
        LocalDate dueDate
) { }