package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import edu.hogwarts.studentadmin.services.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    private final DTOMapper<TeacherDTO, Teacher> teacherDTOMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, DTOMapper<TeacherDTO, Teacher> teacherDTOMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherDTOMapper = teacherDTOMapper;
    }
    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        return teacherDTOMapper.toDTO(teacherRepository.save(teacherDTOMapper.toEntity(teacherDTO)));
    }

    @Override
    public TeacherDTO getTeacherById(int id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        return teacherDTOMapper.toDTO(teacher);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(teacherDTOMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO) {
        Teacher original = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        Teacher teacher = teacherDTOMapper.toEntity(teacherDTO);

        // Update Teacher
        original.setFirstName(teacher.getFirstName());
        original.setMiddleName(teacher.getMiddleName());
        original.setLastName(teacher.getLastName());
        original.setHouse(teacher.getHouse());
        original.setEmployment(teacher.getEmployment());
        original.setDateOfBirth(teacher.getDateOfBirth());
        original.setEmploymentEnd(teacher.getEmploymentEnd());
        original.setEmploymentStart(teacher.getEmploymentStart());
        original.setHeadOfHouse(teacher.isHeadOfHouse());

        Teacher updatedTeacher = teacherRepository.save(original);
        return teacherDTOMapper.toDTO(updatedTeacher);
    }

    @Override
    public void deleteTeacher(int id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        // Remove teacher from courses
        teacher.getCourses().forEach(course -> course.setTeacher(null));
        // Delete the student entity
        teacherRepository.delete(teacher);
    }
}
