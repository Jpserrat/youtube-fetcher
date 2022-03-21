package com.example.youtubechannelfetcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String taskId) {
        super(String.format("Task not found with id %s", taskId));
    }
}
