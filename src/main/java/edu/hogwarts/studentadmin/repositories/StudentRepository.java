package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    List<Student> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    List<Student> findByFirstNameAndLastName(String firstName,String lastName);
    List<Student> findByFirstNameAndMiddleName(String firstName,String middleName);
    List<Student> findByMiddleNameAndLastName(String middleName, String lastName);
    List<Student> findByFirstName(String firstName);
}
