package com.example.youtubechannelfetcher.service;

import com.example.youtubechannelfetcher.data.entity.Task;
import com.example.youtubechannelfetcher.data.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;

    public Task createTask(String channelId) {
        Task task = Task.create(channelId);
        Task createdTask = taskRepository.save(task);

        taskEventProducer.produceTaskCreated(createdTask);

        return createdTask;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void finishTask(Task task) {
        task.finish();
        taskRepository.save(task);
    }

    public void taskFailure(Task task) {
        task.error();
        taskRepository.save(task);
    }

}
