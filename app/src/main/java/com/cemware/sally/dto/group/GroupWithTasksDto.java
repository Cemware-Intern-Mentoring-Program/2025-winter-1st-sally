package com.cemware.sally.dto.group;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.Task;
import java.util.List;

public record GroupWithTasksDto(
        Group group,
        List<Task> tasks
) { }