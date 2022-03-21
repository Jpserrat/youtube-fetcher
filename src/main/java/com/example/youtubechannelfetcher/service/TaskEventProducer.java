package com.example.youtubechannelfetcher.service;

import com.example.youtubechannelfetcher.data.dto.event.TaskCreatedEvent;
import com.example.youtubechannelfetcher.data.entity.Task;
import com.example.youtubechannelfetcher.data.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskEventProducer {

    private final ApplicationEventPublisher producer;
    private final TaskMapper taskMapper;

    public void produceTaskCreated(Task task) {
        TaskCreatedEvent event = taskMapper.domainToEvent(task);
        producer.publishEvent(event);
    }
}
