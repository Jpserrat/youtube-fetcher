package com.example.youtubechannelfetcher.data.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TaskCreateResponse {
    Long taskId;
}
