package com.example.todospring.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    public Todo(String description, boolean starred, boolean completed) {
        this.description = description;
        this.starred = starred;
        this.completed = completed;
    }
}
