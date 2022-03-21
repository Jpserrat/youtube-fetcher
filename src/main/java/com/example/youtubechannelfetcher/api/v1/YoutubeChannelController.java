package com.example.youtubechannelfetcher.api.v1;

import com.example.youtubechannelfetcher.data.dto.response.TaskCreateResponse;
import com.example.youtubechannelfetcher.data.dto.response.TaskGetResponse;
import com.example.youtubechannelfetcher.data.dto.response.TaskListResponse;
import com.example.youtubechannelfetcher.data.entity.Task;
import com.example.youtubechannelfetcher.data.entity.YoutubeVideo;
import com.example.youtubechannelfetcher.data.mapper.TaskMapper;
import com.example.youtubechannelfetcher.data.mapper.YoutubeVideoMapper;
import com.example.youtubechannelfetcher.service.TaskService;
import com.example.youtubechannelfetcher.service.YoutubeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class YoutubeChannelController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeVideoMapper youtubeVideoMapper;

    @GetMapping
    public List<TaskListResponse> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return taskMapper.domainToResponse(tasks);
    }

    @PostMapping("/{channelId}")
    public TaskCreateResponse createTask(@PathVariable String channelId) {
        Task createdTask = taskService.createTask(channelId);
        return taskMapper.domainToResponse(createdTask);
    }

    @GetMapping("/{taskId}")
    public List<TaskGetResponse> getTask(@PathVariable Long taskId) {
        List<YoutubeVideo> videos = youtubeVideoService.getAllByTaskId(taskId);
        return youtubeVideoMapper.domainToResponse(videos);
    }
}
