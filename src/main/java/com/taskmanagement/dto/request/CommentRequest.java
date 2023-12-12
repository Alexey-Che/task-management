package com.taskmanagement.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CommentRequest {

    List<Long> taskIds;
    String text;
}
