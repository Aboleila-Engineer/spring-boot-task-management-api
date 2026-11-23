package com.saad.tasks;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.saad.tasks.TaskDtos.*;
@Service @Transactional
public class TaskService {
    private final TaskRepository tasks;
    private final UserRepository users;
    public TaskService(TaskRepository tasks,UserRepository users) { this.tasks=tasks;this.users=users; }
    public UserView createUser(CreateUser input) {
        String email=input.email().trim().toLowerCase(Locale.ROOT);
        if(users.existsByEmail(email)) throw new ConflictException();
        User u=new User();u.fullName=input.fullName().trim();u.email=email;users.saveAndFlush(u);
        return new UserView(u.id,u.fullName,u.email);
    }
    @Transactional(readOnly=true) public List<UserView> users() { return users.findAll().stream().map(u->new UserView(u.id,u.fullName,u.email)).toList(); }
    public TaskView create(WriteTask input) { Task t=new Task(); apply(t,input);return view(tasks.saveAndFlush(t)); }
    @Transactional(readOnly=true) public List<TaskView> list() { return tasks.findAll().stream().map(this::view).toList(); }
    @Transactional(readOnly=true) public TaskView get(long id) { return view(find(id)); }
    public TaskView update(long id,WriteTask input) { Task t=find(id);apply(t,input);return view(tasks.save(t)); }
    public TaskView complete(long id,CompleteTask input) { Task t=find(id);t.completed=input.completed();return view(tasks.save(t)); }
    public void delete(long id) { tasks.delete(find(id)); }
    private Task find(long id) { return tasks.findById(id).orElseThrow(()->new NotFoundException("Task not found.")); }
    private void apply(Task t,WriteTask input) {
        t.description=input.description().trim();t.completed=input.completed();
        t.user=input.userId()==null?null:users.findById(input.userId()).orElseThrow(()->new NotFoundException("Assigned user not found."));
    }
    private TaskView view(Task t) { return new TaskView(t.id,t.description,t.completed,t.user==null?null:t.user.getId(),t.createdAt); }
}
