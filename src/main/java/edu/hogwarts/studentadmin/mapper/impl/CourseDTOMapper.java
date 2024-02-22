package edu.hogwarts.studentadmin.mapper.impl;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CourseDTOMapper implements DTOMapper<CourseDTO, Course> {

    private DTOMapper<TeacherDTO, Teacher> teacherDTOMapper;
    private DTOMapper<StudentDTO, Student> studentDTOMapper;
    public CourseDTOMapper (DTOMapper<TeacherDTO, Teacher> teacherDTOMapper, DTOMapper<StudentDTO, Student> studentDTOMapper) {
        this.teacherDTOMapper = teacherDTOMapper;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public Course toEntity(CourseDTO dto) {
        return new Course(
                dto.getId(),
                dto.getSubject(),
                dto.getSchoolYear(),
                dto.isCurrent(),
                teacherDTOMapper.toEntity(dto.getTeacher()),
                dto.getStudents().stream().map(studentDTOMapper::toEntity).collect(Collectors.toSet())
        );
    }

    @Override
    public CourseDTO toDTO(Course entity) {
        return new CourseDTO(
                entity.getId(),
                entity.getSubject(),
                entity.getSchoolYear(),
                entity.isCurrent(),
                teacherDTOMapper.toDTO(entity.getTeacher()),
                entity.getStudents().stream().map(studentDTOMapper::toDTO).collect(Collectors.toSet())
        );
    }
}
