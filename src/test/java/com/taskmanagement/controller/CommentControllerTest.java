package com.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.AbstractIntegrationTest;
import com.taskmanagement.dto.request.CommentRequest;
import com.taskmanagement.repository.TaskRepository;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskRepository taskRepository;

    @Test
    @Transactional
    @SneakyThrows
    void setComment() {

        val taskIds = List.of(1L, 2L, 3L);
        val testComment = new CommentRequest(taskIds, "test_comment");

        val tasks = taskRepository.findAllById(taskIds);

        for (var task: tasks) {
            assertNull(task.getComment());
        }

        val objectMapper = new ObjectMapper();
        val content = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(testComment);

        mockMvc.perform(post("/v1/comment")
                        .header("Authorization", ACCESS_HEADER)
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(content))
                .andExpect(status().isOk());

        val tasksWithComment = taskRepository.findAllById(taskIds);

        for (var task: tasksWithComment) {
            assertNotNull(task.getComment());
        }
    }
}