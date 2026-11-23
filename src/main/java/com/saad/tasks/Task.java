package com.saad.tasks;

import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="tasks")
public class Task {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
    @Column(nullable=false,length=500) String description;
    @Column(nullable=false) boolean completed;
    @Column(nullable=false,updatable=false) Instant createdAt;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id") User user;
    protected Task() {}
    @PrePersist void onCreate() { createdAt=Instant.now(); }
}
