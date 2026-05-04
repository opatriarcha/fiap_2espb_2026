package br.com.fiap.espb.es.ddd.taskManager.domainmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="TASKS")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private @Getter @Setter Long id;
    
    @Column(nullable = false, name = "TITLE", length = 100 )
    @NotBlank(message="Title cannot be empty! motherfuckers")
    @Size(min=3, max=100, message="Title must be between 3 and 100 characters")
    private @Getter @Setter String title;
    
    @Column(nullable = true, name = "DESCRIPTION", length = 500 )
    @Size(max=500, message="Description cannot exceed 500 characters")    
    private @Getter @Setter String description;
    
    @NotNull(message="Creation Date cannot be null")
    @Column(nullable = true, name = "CREATION_DATE", insertable = true, unique = false)
    private @Getter @Setter LocalDate creationDate;
    
    @Column(nullable = true, name = "COMPLETION_DATE")
    private @Getter @Setter LocalDate completionDate;
    
    @Column(nullable = true, name = "DUE_DATE")
    private @Getter @Setter LocalDate dueDate;
    
    @Column(nullable = true, name = "INUTIL")
    private @Getter @Setter String inutil;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null.")
    @Column(nullable = false, name = "STATUS")
    private @Getter @Setter TaskStatus status;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority cannot be null.")
    @Column(nullable = false, name = "PRIORITY")
    private @Getter @Setter TaskPriority priority; 
    
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Task other = (Task) obj;
        return Objects.equals(this.id, other.id);
    }
    
    
    
    
    
}
