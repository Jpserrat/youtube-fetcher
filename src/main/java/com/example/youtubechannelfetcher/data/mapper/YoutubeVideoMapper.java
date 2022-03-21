package com.example.youtubechannelfetcher.data.mapper;

import com.example.youtubechannelfetcher.data.dto.response.TaskGetResponse;
import com.example.youtubechannelfetcher.data.entity.YoutubeVideo;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class YoutubeVideoMapper {

    public List<TaskGetResponse> domainToResponse(List<YoutubeVideo> youtubeVideos) {
        return youtubeVideos.stream()
                            .map(video -> TaskGetResponse.builder()
                                 .videoId(video.getVideoId())
                                 .description(video.getDescription())
                                 .link(video.getLink())
                                 .build()).collect(Collectors.toList());
    }

    public List<YoutubeVideo> responseToDomain(List<SearchResult> results, Long taskId) {

        return results.stream()
                      .map(result -> YoutubeVideo.builder()
                                                 .taskId(taskId)
                                                 .videoId(result.getId().getVideoId())
                                                 .description(result.getSnippet().getDescription())
                                                 .link(String.format("https://youtube.com/watch?v=%s", result.getId().getVideoId()))
                                                 .build()).collect(Collectors.toList());

    }
}
