package com.example.youtubechannelfetcher.data.mapper;

import com.example.youtubechannelfetcher.data.dto.event.TaskCreatedEvent;
import com.example.youtubechannelfetcher.data.dto.request.TaskCreateRequest;
import com.example.youtubechannelfetcher.data.dto.response.TaskCreateResponse;
import com.example.youtubechannelfetcher.data.dto.response.TaskListResponse;
import com.example.youtubechannelfetcher.data.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TaskMapper {

    Task requestToDomain(TaskCreateRequest task);

    @Mapping(target = "taskId", source = "id")
    TaskCreateResponse domainToResponse(Task task);

    List<TaskListResponse> domainToResponse(List<Task> tasks);

    TaskCreatedEvent domainToEvent(Task task);
}
