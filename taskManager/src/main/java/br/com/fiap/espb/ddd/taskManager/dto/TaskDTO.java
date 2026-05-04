package br.com.fiap.espb.es.ddd.taskManager.dto;

import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskPriority;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskStatus;
import java.time.LocalDate;
import java.util.Objects;

public class TaskDTO {

    private Long id;
    private String title;
    private String descriptionnnnnnnnnnnnnnn;
    private LocalDate creationDate;
    private LocalDate completionDate;
    private LocalDate dueDate;
    private String inutil;
    private TaskStatus status;
    private TaskPriority priority;

    public TaskDTO(Long id, String title, String description, LocalDate creationDate, LocalDate completionDate, LocalDate dueDate, String inutil, TaskStatus status, TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.descriptionnnnnnnnnnnnnnn = description;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.dueDate = dueDate;
        this.inutil = inutil;
        this.status = status;
        this.priority = priority;
    }

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, LocalDate creationDate, LocalDate completionDate, LocalDate dueDate, TaskStatus status, TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.descriptionnnnnnnnnnnnnnn = description;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return descriptionnnnnnnnnnnnnnn;
    }

    public void setDescription(String description) {
        this.descriptionnnnnnnnnnnnnnn = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getInutil() {
        return inutil;
    }

    public void setInutil(String inutil) {
        this.inutil = inutil;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskDTO other = (TaskDTO) obj;
        return Objects.equals(this.id, other.id);
    }
}