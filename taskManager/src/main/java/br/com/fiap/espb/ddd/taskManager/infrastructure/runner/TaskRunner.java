package br.com.fiap.espb.es.ddd.taskManager.infrastructure.runner;

import br.com.fiap.espb.es.ddd.taskManager.datasource.repository.TaskRepository;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.Task;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskPriority;
import br.com.fiap.espb.es.ddd.taskManager.domainmodel.TaskStatus;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskRunner {

    @Bean
    CommandLineRunner initData(TaskRepository taskRepository) {
        return args -> {
            // Creating 10 instances of Task
            Task task1 = new Task();
            task1.setTitle("Complete project proposal");
            task1.setDescription("Draft the initial project proposal for client review");
            task1.setPriority(TaskPriority.HIGH);
            task1.setStatus(TaskStatus.PENDING);
            task1.setDueDate(LocalDate.of(2025, 5, 10));
            task1.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task2 = new Task();
            task2.setTitle("Update database schema");
            task2.setDescription("Modify database schema to support new features");
            task2.setPriority(TaskPriority.MEDIUM);
            task2.setStatus(TaskStatus.IN_PROGRESS);
            task2.setDueDate(LocalDate.of(2025, 5, 15));
            task2.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task3 = new Task();
            task3.setTitle("Write unit tests");
            task3.setDescription("Create unit tests for authentication module");
            task3.setPriority(TaskPriority.MEDIUM);
            task3.setStatus(TaskStatus.PENDING);
            task3.setDueDate(LocalDate.of(2025, 5, 20));
            task3.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task4 = new Task();
            task4.setTitle("Team meeting");
            task4.setDescription("Weekly sync with development team");
            task4.setPriority(TaskPriority.LOW);
            task4.setStatus(TaskStatus.PENDING);
            task4.setDueDate(LocalDate.of(2025, 5, 7));
            task4.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task5 = new Task();
            task5.setTitle("Fix UI bugs");
            task5.setDescription("Resolve alignment issues in dashboard");
            task5.setPriority(TaskPriority.HIGH);
            task5.setStatus(TaskStatus.IN_PROGRESS);
            task5.setDueDate(LocalDate.of(2025, 5, 8));
            task5.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task6 = new Task();
            task6.setTitle("Deploy to production");
            task6.setDescription("Release version 2.1 to production");
            task6.setPriority(TaskPriority.HIGH);
            task6.setStatus(TaskStatus.PENDING);
            task6.setDueDate(LocalDate.of(2025, 5, 12));
            task6.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task7 = new Task();
            task7.setTitle("Review code");
            task7.setDescription("Perform code review for pull request #123");
            task7.setPriority(TaskPriority.MEDIUM);
            task7.setStatus(TaskStatus.COMPLETED);
            task7.setDueDate(LocalDate.of(2025, 5, 5));
            task7.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task8 = new Task();
            task8.setTitle("Update documentation");
            task8.setDescription("Update API documentation for new endpoints");
            task8.setPriority(TaskPriority.LOW);
            task8.setStatus(TaskStatus.PENDING);
            task8.setDueDate(LocalDate.of(2025, 5, 25));
            task8.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task9 = new Task();
            task9.setTitle("Optimize performance");
            task9.setDescription("Improve query performance for reports");
            task9.setPriority(TaskPriority.MEDIUM);
            task9.setStatus(TaskStatus.IN_PROGRESS);
            task9.setDueDate(LocalDate.of(2025, 5, 18));
            task9.setCreationDate(LocalDate.of(2025, 5, 10));

            Task task10 = new Task();
            task10.setTitle("Client feedback session");
            task10.setDescription("Gather feedback on beta release");
            task10.setPriority(TaskPriority.HIGH);
            task10.setStatus(TaskStatus.PENDING);
            task10.setDueDate(LocalDate.of(2025, 5, 30));
            task10.setCreationDate(LocalDate.of(2025, 5, 10));

            // Printing tasks to verify
            System.out.println("Task 1: " + task1.getTitle() + ", Priority: " + task1.getPriority());
            System.out.println("Task 2: " + task2.getTitle() + ", Priority: " + task2.getPriority());
            System.out.println("Task 3: " + task3.getTitle() + ", Priority: " + task3.getPriority());
            System.out.println("Task 4: " + task4.getTitle() + ", Priority: " + task4.getPriority());
            System.out.println("Task 5: " + task5.getTitle() + ", Priority: " + task5.getPriority());
            System.out.println("Task 6: " + task6.getTitle() + ", Priority: " + task6.getPriority());
            System.out.println("Task 7: " + task7.getTitle() + ", Priority: " + task7.getPriority());
            System.out.println("Task 8: " + task8.getTitle() + ", Priority: " + task8.getPriority());
            System.out.println("Task 9: " + task9.getTitle() + ", Priority: " + task9.getPriority());
            System.out.println("Task 10: " + task10.getTitle() + ", Priority: " + task10.getPriority());

            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
            taskRepository.save(task4);
            taskRepository.save(task5);
            taskRepository.save(task6);
            taskRepository.save(task7);
            taskRepository.save(task8);
            taskRepository.save(task9);
            taskRepository.save(task10);
        };
    }
}
