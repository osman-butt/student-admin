package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    @Query("SELECT s FROM Student s " +
            "WHERE (:firstName IS NULL OR s.firstName = :firstName) " +
            "AND (:middleName IS NULL OR s.middleName = :middleName OR s.middleName IS NULL) " +
            "AND (:lastName IS NULL OR s.lastName = :lastName OR s.middleName IS NULL)")
    List<Student> findByFullName(@Param("firstName") String firstName,
                                 @Param("middleName") String middleName,
                                 @Param("lastName") String lastName);
}
