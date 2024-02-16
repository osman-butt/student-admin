package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
}
