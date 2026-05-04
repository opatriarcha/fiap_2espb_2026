package br.com.fiap.espb.ddd.taskManager.controllers;


import br.com.fiap.espb.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.ddd.taskManager.domainmodel.TaskPriority;
import br.com.fiap.espb.ddd.taskManager.domainmodel.TaskStatus;
import br.com.fiap.espb.ddd.taskManager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String listTasks( Model model ) {
        model.addAttribute("tasks", this.taskService.findAll());
        return "tasks/list";
    }

    @PostMapping("/save")
    public String saveTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult, Model model){
        if( bindingResult.hasErrors() ){
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "tasks/form";
        }
        this.taskService.save(task);
        return "redirect:/tasks";
    }

}
