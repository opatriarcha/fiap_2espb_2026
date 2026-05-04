package br.com.fiap.espb.es.ddd.taskManager.mapper;

import br.com.fiap.espb.es.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.es.ddd.taskManager.dto.TaskDTO;

public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getCreationDate(),
            task.getCompletionDate(),
            task.getDueDate(),
                task.getInutil(),
            task.getStatus(),
            task.getPriority()
        );
    }

    public Task toEntity(TaskDTO dto) {
        if (dto == null) {
            return null;
        }
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCreationDate(dto.getCreationDate());
        task.setCompletionDate(dto.getCompletionDate());
        task.setDueDate(dto.getDueDate());
        task.setInutil(dto.getInutil());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        return task;
    }
}