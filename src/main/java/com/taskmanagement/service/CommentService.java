package com.taskmanagement.service;

import com.taskmanagement.dto.request.CommentRequest;
import com.taskmanagement.entity.Comment;
import com.taskmanagement.exception.TaskNotFoundException;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {

    CommentRepository commentRepository;
    TaskRepository taskRepository;

    @Transactional
    public void setComment(CommentRequest request) {
        var tasks = taskRepository.findAllById(request.getTaskIds());

        if (tasks.isEmpty()) {
            throw new TaskNotFoundException();
        }

        var comment = commentRepository.save(Comment.builder()
                .text(request.getText())
                .tasks(tasks)
                .build());

        tasks.forEach(t -> t.setComment(comment));
    }
}
