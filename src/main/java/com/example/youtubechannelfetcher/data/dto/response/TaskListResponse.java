package com.example.youtubechannelfetcher.data.dto.response;

import com.example.youtubechannelfetcher.data.entity.TaskStatus;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TaskListResponse {

    Long id;

    String channelId;

    TaskStatus status;
}
