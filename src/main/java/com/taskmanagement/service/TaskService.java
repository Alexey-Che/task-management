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
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository taskRepository;
    UserRepository userRepository;
    UserService userService;

    public Task getTask(Long id) {
        val currentUser = userService.getCurrentUser();
        return taskRepository.findByIdAndAuthorOrExecutor(id, currentUser).orElseThrow(TaskNotFoundException::new);
    }

    public List<Task> getAllUserTasks(Long userId) {
        val user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return taskRepository.findByAuthorIdOrExecutorId(user);
    }

    @Transactional
    public void deleteTask(Long id) {
        val currentUserId = userService.getCurrentUserId();
        val task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        if (task.getAuthor().getId().equals(currentUserId) || task.getExecutor().getId().equals(currentUserId)) {
            taskRepository.delete(task);
        }
    }

    @Transactional
    public Task createTask(TaskDto task) {
        val currentUser = userService.getCurrentUser();
        var newTask = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus() == null ? TaskStatus.PENDING : task.getStatus())
                .priority(task.getPriority() == null ? TaskPriority.MEDIUM : task.getPriority())
                .author(currentUser);

        if (task.getExecutorId() != null) {
            newTask.executor(userRepository.findById(task.getExecutorId())
                    .orElseThrow(() -> new UserNotFoundException("executor not found")));
        }

        return taskRepository.save(newTask.build());
    }

    @Transactional
    public void changeTaskStatus(Long taskId, TaskStatus status) {
        val currentUser = userService.getCurrentUser();
        var task = taskRepository.findByIdAndAuthorOrExecutor(taskId, currentUser)
                .orElseThrow(TaskNotFoundException::new);
        task.setStatus(status);
    }

    @Transactional
    public Task setTaskExecutor(Long taskId, Long executorUserId) {
        val currentUser = userService.getCurrentUser();
        var task = taskRepository.findByIdAndAuthor(taskId, currentUser).orElseThrow(TaskNotFoundException::new);
        val executor = userRepository.findById(executorUserId).orElseThrow(UserNotFoundException::new);

        task.setExecutor(executor);

        return taskRepository.save(task);
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
