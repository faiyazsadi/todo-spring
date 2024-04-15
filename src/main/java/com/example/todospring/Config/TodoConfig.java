package com.example.todospring.Config;

import com.example.todospring.Model.Todo;
import com.example.todospring.Repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TodoConfig {
    @Bean
    CommandLineRunner commandLineRunner(TodoRepository todoRepository) {
        return args -> {
            Todo todo1 = new Todo(
                    "this is a new todo",
                    false,
                    false
            );
            Todo todo2 = new Todo(
                    "this is another todo",
                    false,
                    false
            );
            Todo todo3 = new Todo(
                3L,
                "add another toto",
                false,
                false
            );
            todoRepository.saveAll(
                    List.of(todo1, todo2, todo3)
            );
        };
    }
}
