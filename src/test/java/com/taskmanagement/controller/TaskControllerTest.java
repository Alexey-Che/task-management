package com.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.AbstractIntegrationTest;
import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.enums.TaskPriority;
import com.taskmanagement.entity.enums.TaskStatus;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
class TaskControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    void getTask() {

        mockMvc.perform(get("/v1/task/1")
                        .header("Authorization", ACCESS_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("task_1"))
                .andExpect(jsonPath("$.description").value("description_1"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.author.id").value("1"));
    }

    @Test
    @SneakyThrows
    void getAllUserTasks() {
        mockMvc.perform(get("/v1/task")
                        .header("Authorization", ACCESS_HEADER)
                        .param("userId", "" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }


    @Test
    @Transactional
    @SneakyThrows
    void createTask() {
        val newTask = TaskDto.builder()
                .title("test_title")
                .description("description")
                .executorId(1L)
                .authorId(1L)
                .build();

        val objectMapper = new ObjectMapper();

        val content = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(newTask);

        val response = mockMvc.perform(put("/v1/task")
                        .header("Authorization", ACCESS_HEADER)
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(Charset.defaultCharset());

        val createdTask = objectMapper.readValue(response, Task.class);

        assertEquals(newTask.getTitle(), createdTask.getTitle());
        assertEquals(newTask.getDescription(), createdTask.getDescription());
        assertEquals(createdTask.getStatus(), TaskStatus.PENDING);
        assertEquals(createdTask.getPriority(), TaskPriority.MEDIUM);
        assertNotNull(createdTask.getAuthor());
        assertNotNull(createdTask.getExecutor());
    }

    @Test
    @Transactional
    @SneakyThrows
    void editTask() {

        val edit = TaskDto.builder()
                .id(3L)
                .title("edited_title")
                .description("edited_description")
                .build();

        val objectMapper = new ObjectMapper();

        val content = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(edit);

        val response = mockMvc.perform(post("/v1/task")
                        .header("Authorization", ACCESS_HEADER)
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(Charset.defaultCharset());

        val editedTask = objectMapper.readValue(response, Task.class);

        assertEquals(edit.getTitle(), editedTask.getTitle());
        assertEquals(edit.getDescription(), editedTask.getDescription());
    }
}