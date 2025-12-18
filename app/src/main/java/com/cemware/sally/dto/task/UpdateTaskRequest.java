package com.cemware.sally.dto.task;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        String status,
        LocalDate dueDate
) { }