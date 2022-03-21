package com.example.youtubechannelfetcher.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String channelId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public Task finish() {
        this.setStatus(TaskStatus.FINISHED);
        return this;
    }

    public Task error() {
        this.setStatus(TaskStatus.ERROR);
        return this;
    }

    public static Task create(String channelId) {
        return new Task(null, channelId, TaskStatus.IN_PROGRESS);
    }

}
