package com.cemware.sally.dto.task;

import com.cemware.sally.domain.TaskStatus;

import java.time.LocalDate;

public record TaskUpdateDto(
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate
) {
}
