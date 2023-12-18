package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.entity.enums.TaskStatus;
import com.taskmanagement.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/task")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskService taskService;

    @GetMapping("{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllUserTasks(@RequestParam("userId") Long userId,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "limit", defaultValue = "3") Integer limit,
                                             @RequestParam(value = "sort", defaultValue = "status") String sort) {
        return ResponseEntity.ok(taskService.getAllUserTasks(userId, page, limit, sort));
    }

    @PutMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @PostMapping
    public ResponseEntity<?> editTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.editTask(taskDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/executor")
    public ResponseEntity<?> setTaskExecutor(@PathVariable("id") Long taskId, @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.setTaskExecutor(taskId, userId));
    }

    @PostMapping("{id}/status")
    public ResponseEntity<?> setTaskStatus(@PathVariable("id") Long taskId, @RequestParam("status") TaskStatus status) {
        taskService.changeTaskStatus(taskId, status);
        return ResponseEntity.ok().build();
    }

}
