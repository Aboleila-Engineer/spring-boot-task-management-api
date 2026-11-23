package com.saad.tasks;

import jakarta.validation.constraints.*;
import java.time.Instant;
public final class TaskDtos {
    private TaskDtos() {}
    public record CreateUser(@NotBlank @Size(max=120) String fullName,@NotBlank @Email @Size(max=254) String email) {}
    public record UserView(Long id,String fullName,String email) {}
    public record WriteTask(@NotBlank @Size(max=500) String description,@NotNull Boolean completed,Long userId) {}
    public record CompleteTask(@NotNull Boolean completed) {}
    public record TaskView(Long id,String description,boolean completed,Long userId,Instant createdAt) {}
}
