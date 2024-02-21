package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    TeacherDTO createTeacher(TeacherDTO teacherDTO);
    TeacherDTO getTeacherById(int id);
    List<TeacherDTO> getAllTeachers();
    TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO);
    void deleteTeacher(int id);
}
