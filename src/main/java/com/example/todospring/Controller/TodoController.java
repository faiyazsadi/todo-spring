package com.example.todospring.Controller;

import com.example.todospring.Model.Todo;
import com.example.todospring.Service.PdfGenerationService;
import com.example.todospring.Service.TodoService;
import com.example.todospring.dto.TodoCreateDto;
import com.example.todospring.dto.TodoUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/todos")
public class TodoController {
    private final TodoService todoService;
    @Autowired
    private PdfGenerationService pdfGenerationService;
    @Autowired
    private ResourceLoader resourceLoader;
    private final TemplateEngine templateEngine;
    @Autowired
    public TodoController(TodoService todoService, TemplateEngine templateEngine) {
        this.todoService = todoService;
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public String getTodos(Model model) {
        List<Todo> todoList = todoService.getTodos();
        Collections.sort(todoList, Comparator.comparingLong(Todo::getId));
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
        return "redirect:/todos";
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            // Prepare data for the PDF
            Context context = new Context();
            context.setVariable("todos", todoService.getTodos());


            // Process the Thymeleaf template
            String htmlContent = templateEngine.process("thymeleaf_template", context);
            System.out.println(htmlContent);
            // Generate PDF from HTML content
            byte[] pdfBytes = generatePdfFromHtml(htmlContent);

            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "generated.pdf");


            // Return the PDF as a byte array
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private byte[] generatePdfFromHtml(String htmlContent) throws IOException, com.lowagie.text.DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return outputStream.toByteArray();
        }
    }
}
