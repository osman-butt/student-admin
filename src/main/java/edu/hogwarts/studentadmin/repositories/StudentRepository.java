package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {
}
