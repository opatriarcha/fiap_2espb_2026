package br.com.fiap.espb.es.ddd.taskManager.datasource.repository;

import br.com.fiap.espb.es.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository  extends JpaRepository<Task, Long>{
    public List<Task> findAllByOrderByDueDateAsc();
    public List<Task> findByStatusOrderByDueDate(TaskStatus status);
    
}
