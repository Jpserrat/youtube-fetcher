package com.example.youtubechannelfetcher.data.dto.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TaskCreateRequest {

    String channelId;

}
