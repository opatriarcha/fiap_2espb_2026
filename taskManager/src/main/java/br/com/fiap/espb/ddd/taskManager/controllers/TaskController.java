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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping("/new")
    public String showCreateForm( Model model ){
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "tasks/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(
            @PathVariable("id") Long id,
            Model model){
        this.taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model){
        Optional<Task> newTask = this.taskService.findById(id);
        if( newTask.isEmpty()){
            model.addAttribute("task",  new Task());
            return "tasks/form";
        }else{
            model.addAttribute("task", newTask.get());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "tasks/form";
        }
    }

    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable("id") Long id, Model model){
        Task task = this.taskService.findById(id).orElse(null);
        model.addAttribute("task", task);
        if(task == null)
            return "redirect:/tasks";
        model.addAttribute("task", task);
        return "tasks/view";
    }





}
