package com.saad.tasks;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.saad.tasks.TaskDtos.*;
@RestController @RequestMapping("/api")
public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service) { this.service=service; }
    @PostMapping("/users") ResponseEntity<UserView> createUser(@Valid @RequestBody CreateUser input) { UserView u=service.createUser(input);return ResponseEntity.created(URI.create("/api/users")).body(u); }
    @GetMapping("/users") List<UserView> users() { return service.users(); }
    @PostMapping("/tasks") ResponseEntity<TaskView> create(@Valid @RequestBody WriteTask input) { TaskView t=service.create(input);return ResponseEntity.created(URI.create("/api/tasks/"+t.id())).body(t); }
    @GetMapping("/tasks") List<TaskView> list() { return service.list(); }
    @GetMapping("/tasks/{id}") TaskView get(@PathVariable long id) { return service.get(id); }
    @PutMapping("/tasks/{id}") TaskView update(@PathVariable long id,@Valid @RequestBody WriteTask input) { return service.update(id,input); }
    @PatchMapping("/tasks/{id}") TaskView complete(@PathVariable long id,@Valid @RequestBody CompleteTask input) { return service.complete(id,input); }
    @DeleteMapping("/tasks/{id}") ResponseEntity<Void> delete(@PathVariable long id) { service.delete(id);return ResponseEntity.noContent().build(); }
}
