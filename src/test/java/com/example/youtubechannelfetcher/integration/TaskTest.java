package com.example.youtubechannelfetcher.integration;

import com.example.youtubechannelfetcher.data.dto.event.TaskCreatedEvent;
import com.example.youtubechannelfetcher.data.dto.response.TaskCreateResponse;
import com.example.youtubechannelfetcher.data.dto.response.TaskGetResponse;
import com.example.youtubechannelfetcher.data.dto.response.TaskListResponse;
import com.example.youtubechannelfetcher.data.entity.Task;
import com.example.youtubechannelfetcher.data.entity.TaskStatus;
import com.example.youtubechannelfetcher.data.entity.YoutubeVideo;
import com.example.youtubechannelfetcher.data.mapper.TaskMapper;
import com.example.youtubechannelfetcher.data.repository.TaskRepository;
import com.example.youtubechannelfetcher.data.repository.YoutubeVideoRepository;
import com.example.youtubechannelfetcher.mock.SearchResultMock;
import com.example.youtubechannelfetcher.service.TaskEventListener;
import com.example.youtubechannelfetcher.service.TaskEventProducer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Testcontainers
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskTest {
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private YoutubeVideoRepository youtubeVideoRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskEventListener taskEventListener;

    @MockBean
    private TaskEventProducer taskEventProducer;

    @MockBean
    private YoutubeClient youtubeClient;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    public static Task task;

    public static String channelId = "channelId";

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/tasks";
    }

    @Test
    @Order(1)
    public void createTask() throws GeneralSecurityException, IOException {
        //given
        doNothing().when(taskEventProducer).produceTaskCreated(any());

        //when
        TaskCreateResponse taskCreateResponse = this.restTemplate.postForObject(baseUrl + "/" + channelId, "", TaskCreateResponse.class);

        //then
        Optional<Task> task = taskRepository.findById(taskCreateResponse.getTaskId());
        assertTrue(task.isPresent());
        assertEquals(TaskStatus.IN_PROGRESS, task.get().getStatus());
        this.task = task.get();
    }

    @Test
    @Order(2)
    public void processTask() throws GeneralSecurityException, IOException {
        //given
        TaskCreatedEvent event = taskMapper.domainToEvent(task);
        SearchListResponse expectedSearchResult = SearchResultMock.getSearchListResponse();
        when(youtubeClient.getNextPage(any(), any())).thenReturn(expectedSearchResult);

        //when
        taskEventListener.taskCreatedEventListener(event);

        //then
        SearchResult expectedItem = expectedSearchResult.getItems().get(0);
        List<YoutubeVideo> videos = youtubeVideoRepository.findAllByTaskId(task.getId());
        Optional<Task> currentTask = taskRepository.findById(task.getId());
        assertTrue(currentTask.isPresent());
        assertEquals(TaskStatus.FINISHED, currentTask.get().getStatus());
        assertEquals(1, videos.size());
        assertEquals(expectedItem.getId().getVideoId(), videos.get(0).getVideoId());
        assertEquals("https://youtube.com/watch?v=" + expectedItem.getId().getVideoId(), videos.get(0).getLink());
        assertEquals(expectedItem.getSnippet().getDescription(), videos.get(0).getDescription());
    }

    @Test
    @Order(3)
    public void getTask() {
        TaskGetResponse[] taskGetResponses = restTemplate.getForObject(baseUrl + "/" + 1, TaskGetResponse[].class);

        SearchResult expected = SearchResultMock.getSearchResult();
        assertEquals(1, taskGetResponses.length);
        assertEquals(expected.getSnippet().getDescription(), taskGetResponses[0].getDescription());
        assertEquals(expected.getId().getVideoId(), taskGetResponses[0].getVideoId());
    }



    @Test
    @Order(3)
    public void getTasks() {
        TaskListResponse[] taskGetResponses = restTemplate.getForObject(baseUrl, TaskListResponse[].class);

        assertEquals(1, taskGetResponses.length);
        assertEquals(channelId, taskGetResponses[0].getChannelId());
        assertEquals(TaskStatus.FINISHED, taskGetResponses[0].getStatus());
    }

}
