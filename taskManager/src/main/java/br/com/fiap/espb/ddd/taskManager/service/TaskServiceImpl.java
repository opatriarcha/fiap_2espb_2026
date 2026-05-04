package br.com.fiap.espb.es.ddd.taskManager.service;

import br.com.fiap.espb.es.ddd.taskManager.datasource.repository.TaskRepository;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;

    @Override
    public List<Task> findAllByOrderByDueDateAsc() {
        return taskRepository.findAllByOrderByDueDateAsc();
    }

    @Override
    public List<Task> findByStatusOrderByDueDate(TaskStatus status) {
        return taskRepository.findByStatusOrderByDueDate(status);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return taskRepository.findAll(sort);
    }

    @Override
    public <S extends Task> S save(S entity) {
        return taskRepository.save(entity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    @Override
    public long count() {
        return taskRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void delete(Task entity) {
        taskRepository.delete(entity);
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
    
    public TaskServiceImpl( final TaskRepository taskRepository ){
        this.taskRepository = taskRepository;
    }
}
