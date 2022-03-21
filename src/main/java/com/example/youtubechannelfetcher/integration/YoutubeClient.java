package com.example.youtubechannelfetcher.integration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


@Service
public class YoutubeClient {

    private static final String APPLICATION_NAME = "Youtube fetcher";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new YouTube.Builder(httpTransport, JSON_FACTORY, request -> {})
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private YouTube.Search.List getSearch(String channelId) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();

        YouTube.Search.List request = youtubeService.search()
                                                    .list(List.of("snippet"));
        return  request
                .setKey("apiKey")
                .setChannelId(channelId)
                .setMaxResults(1000000L);
    }

    public SearchListResponse getNextPage(String channelId, String nextPageToken) throws GeneralSecurityException, IOException {
        YouTube.Search.List request = getSearch(channelId);

        return request.setPageToken(nextPageToken).execute();
    }

}
