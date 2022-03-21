package com.example.youtubechannelfetcher.data.repository;

import com.example.youtubechannelfetcher.data.entity.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findAllByTaskId(Long taskId);
}
