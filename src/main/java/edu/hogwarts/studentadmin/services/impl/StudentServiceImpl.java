package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final DTOMapper<StudentDTO,Student> studentDTOMapper;

    public StudentServiceImpl(StudentRepository studentRepository, DTOMapper<StudentDTO,Student> studentDTOMapper) {
        this.studentRepository = studentRepository;
        this.studentDTOMapper = studentDTOMapper;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        return studentDTOMapper.toDTO(studentRepository.save(studentDTOMapper.toEntity(studentDTO)));
    }

    @Override
    public StudentDTO getStudentById(int id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));
        return studentDTOMapper.toDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(studentDTOMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateStudent(int id, StudentDTO studentDTO) {
        Student original = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));

        // Map DTO to entity
        Student student = studentDTOMapper.toEntity(studentDTO);

        // Update student information
        original.setFirstName(student.getFirstName());
        original.setMiddleName(student.getMiddleName());
        original.setLastName(student.getLastName());
        original.setHouse(student.getHouse());
        original.setGraduated(student.isGraduated());
        original.setDateOfBirth(student.getDateOfBirth());
        original.setEnrollmentYear(student.getEnrollmentYear());
        original.setPrefect(student.isPrefect());
        original.setGraduationYear(student.getGraduationYear());
        original.setSchoolYear(student.getSchoolYear());

        Student updatedStudent = studentRepository.save(original);
        return studentDTOMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(int id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));
        // Remove student from courses
        student.getCourses().forEach(course -> course.getStudents().remove(student));
        // Delete the student entity
        studentRepository.delete(student);
    }
}
