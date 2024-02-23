package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends Service<TeacherDTO, Teacher> {
    TeacherDTO create(TeacherDTO teacherDTO);
    Optional<TeacherDTO> findById(int id);
    List<TeacherDTO> findAll();
    TeacherDTO update(int id, TeacherDTO teacherDTO);
    void delete(int id);
}
