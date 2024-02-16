package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Integer> {
}
