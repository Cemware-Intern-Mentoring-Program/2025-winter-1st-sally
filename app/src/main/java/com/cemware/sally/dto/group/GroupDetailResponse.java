package com.cemware.sally.dto.group;

import com.cemware.sally.dto.task.TaskResponse;
import java.util.List;

public record GroupDetailResponse(
        Long id,
        String name,
        String description,
        List<TaskResponse> tasks
) { }