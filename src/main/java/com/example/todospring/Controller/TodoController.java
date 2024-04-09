package com.example.todospring.Controller;

import com.example.todospring.Model.Todo;
import com.example.todospring.Service.TodoService;
import com.example.todospring.dto.TodoCreateDto;
import com.example.todospring.dto.TodoUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping
    public String getTodos(Model model) {
        List<Todo> todoList = todoService.getTodos();
        model.addAttribute("todos", todoList);
        return "todos/index";
    }

    @PostMapping("/add")
    public  String addTodo(@ModelAttribute TodoCreateDto todoCreateDto) {
        Todo addTodo = new Todo(todoCreateDto.getDescription(), todoCreateDto.isStarred(), todoCreateDto.isCompleted());
        todoService.save(addTodo);
        return "redirect:/todos";
    }
    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable("id") Long id, @ModelAttribute TodoUpdateDto todoUpdateDto) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if(optionalTodo.isEmpty()) {
            return "todos/error";
        }
        Todo updateTodo = optionalTodo.get();
        updateTodo.setDescription(todoUpdateDto.getDescription());
        updateTodo.setStarred(todoUpdateDto.isStarred());
        updateTodo.setCompleted(todoUpdateDto.isCompleted());

        todoService.save(updateTodo);
        System.out.println("UPDATE");
        return "redirect:/todos";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id) {
        Optional<Todo> todoOptional = todoService.findById(id);
        if(todoOptional.isEmpty()) {
            return "todos/error";
        }
        Todo deleteTodo = todoOptional.get();

        todoService.delete(deleteTodo);
        System.out.println("DELETE");
        return "redirect:/todos";
    }

}
