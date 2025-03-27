package ru.home_work.t1_school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.home_work.t1_school.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("update Task t set t = :task where t.id = :id")
    void updateTask(@Param("id") Long id,
                    @Param("task") Task task);
}
