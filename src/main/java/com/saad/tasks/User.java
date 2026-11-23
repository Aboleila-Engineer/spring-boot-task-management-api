package com.saad.tasks;

import jakarta.persistence.*;
@Entity @Table(name="app_users",uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
    @Column(nullable=false,length=120) String fullName;
    @Column(nullable=false,length=254) String email;
    protected User() {}
    public Long getId() { return id; }
}
