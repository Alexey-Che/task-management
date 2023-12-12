package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.enums.TaskPriority;
import com.taskmanagement.entity.enums.TaskStatus;
import com.taskmanagement.exception.TaskNotFoundException;
import com.taskmanagement.exception.UserNotFoundException;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.delete(getTask(id));
    }

    @Transactional
    public Task createTask(TaskDto task) {
        var newTask = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus() == null ? TaskStatus.PENDING : task.getStatus())
                .priority(task.getPriority() == null ? TaskPriority.MEDIUM : task.getPriority());

        if (task.getAuthorId() != null) {
            newTask.author(userRepository.findById(task.getAuthorId())
                    .orElseThrow(() -> new UserNotFoundException("author not found")));
        }

        if (task.getExecutorId() != null) {
            newTask.executor(userRepository.findById(task.getExecutorId())
                    .orElseThrow(() -> new UserNotFoundException("executor not found")));
        }

        return taskRepository.save(newTask.build());
    }

    @Transactional
    public Task editTask(TaskDto taskDto) {
        var editedTask = getTask(taskDto.getId());

        editedTask.setTitle(taskDto.getTitle());
        editedTask.setDescription(taskDto.getDescription());
        editedTask.setStatus(taskDto.getStatus());
        editedTask.setPriority(taskDto.getPriority());

        if (taskDto.getAuthorId() != null) {
            editedTask.setAuthor(userRepository.findById(taskDto.getAuthorId())
                    .orElseThrow(() -> new UserNotFoundException("author not found")));
        }
        if (taskDto.getExecutorId() != null) {
            editedTask.setExecutor(userRepository.findById(taskDto.getExecutorId())
                    .orElseThrow(() -> new UserNotFoundException("executor not found")));
        }

        return taskRepository.save(editedTask);
    }

}
