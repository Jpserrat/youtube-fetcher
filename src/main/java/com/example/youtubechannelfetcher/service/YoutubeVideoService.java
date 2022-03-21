package com.example.youtubechannelfetcher.service;

import com.example.youtubechannelfetcher.data.entity.YoutubeVideo;
import com.example.youtubechannelfetcher.data.mapper.YoutubeVideoMapper;
import com.example.youtubechannelfetcher.data.repository.YoutubeVideoRepository;
import com.example.youtubechannelfetcher.integration.YoutubeClient;
import com.google.api.services.youtube.model.SearchListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class YoutubeVideoService {
    private final YoutubeClient youtubeClient;
    private final YoutubeVideoRepository youtubeVideoRepository;
    private final YoutubeVideoMapper youtubeVideoMapper;

    public List<YoutubeVideo> getAllByTaskId(Long taskId) {
        return youtubeVideoRepository.findAllByTaskId(taskId);
    }

    public void saveYoutubeVideos(String channelId, Long taskId) throws GeneralSecurityException, IOException {

        String nextPageToken = "";

        while (nonNull(nextPageToken)) {
            SearchListResponse nextPage = youtubeClient.getNextPage(channelId, nextPageToken);
            List<YoutubeVideo> youtubeVideoList = youtubeVideoMapper.responseToDomain(nextPage.getItems(), taskId);
            youtubeVideoRepository.saveAll(youtubeVideoList);
            nextPageToken = nextPage.getNextPageToken();
        }
    }


}
