package com.example.todospring.Model;

import jakarta.persistence.*;

@Entity
@Table
public class Todo {
    @Id
    @SequenceGenerator(
            name = "todo_sequence",
            sequenceName = "todo_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "todo_sequence"
    )
    private Long id;
    private String description;
    private boolean starred;
    private boolean completed;
    public Todo() {
    }

    public Todo(Long id, String description, boolean starred, boolean completed) {
        this.id = id;
        this.description = description;
        this.starred = starred;
        this.completed = completed;
    }

    public Todo(String description, boolean starred, boolean completed) {
        this.description = description;
        this.starred = starred;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
