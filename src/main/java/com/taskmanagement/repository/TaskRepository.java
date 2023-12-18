package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.id = :taskId and (t.author = :user or t.executor = :user)")
    Optional<Task> findByIdAndAuthorOrExecutor(Long taskId, User user);

    @Query("select t from Task t where t.author = :user or t.executor = :user")
    List<Task> findByAuthorIdOrExecutorId(User user, Pageable page);

    @Query("select t from Task t where t.id = :taskId and t.author = :author")
    Optional<Task> findByIdAndAuthor(Long taskId, User author);

}
