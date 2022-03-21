package com.example.youtubechannelfetcher.service;

import com.example.youtubechannelfetcher.data.dto.event.TaskCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskEventListener {

    private final TaskService taskService;
    private final YoutubeVideoService youtubeVideoService;

    @Async
    @EventListener
    public void taskCreatedEventListener(TaskCreatedEvent event) {
        taskService.getTaskById(event.getId()).ifPresent(task -> {
            try {
                youtubeVideoService.saveYoutubeVideos(task.getChannelId(), task.getId());
                taskService.finishTask(task);
            } catch (Exception e) {
                taskService.taskFailure(task);
            }
        });
    }
}
