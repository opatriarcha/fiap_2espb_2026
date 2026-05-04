package br.com.fiap.espb.es.ddd.taskManager.service;

import br.com.fiap.espb.es.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public interface TaskService {

    public long count();
    public void delete(Task entity);
    public void deleteById(Long id);
    public boolean existsById(Long id);
    public List<Task> findAll();
    public List<Task> findAll(Sort sort);
    public Page<Task> findAll(Pageable pageable);
    public List<Task> findAllByOrderByDueDateAsc();
    public Optional<Task> findById(Long id);
    public List<Task> findByStatusOrderByDueDate(TaskStatus status);
    public <S extends Task> S save(S entity);
    
}
