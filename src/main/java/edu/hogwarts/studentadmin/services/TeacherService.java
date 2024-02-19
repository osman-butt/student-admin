package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    Teacher createTeacher(Teacher teacher);
    Optional<Teacher> getTeacherById(int id);
    List<Teacher> getAllTeachers();
    Teacher updateTeacher(int id, Teacher teacher);
    void deleteTeacher(int id);
}
