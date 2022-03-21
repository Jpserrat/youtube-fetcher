package com.example.youtubechannelfetcher.data.dto.event;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class TaskCreatedEvent {
    Long id;
}
