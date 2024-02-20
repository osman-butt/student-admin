package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.StudentReqDTO;
import edu.hogwarts.studentadmin.dto.StudentReqDTOMapper;
import edu.hogwarts.studentadmin.dto.StudentResDTO;
import edu.hogwarts.studentadmin.dto.StudentResDTOMapper;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentResDTOMapper studentResDTOMapper;
    private final StudentReqDTOMapper studentReqDTOMapper;

    public StudentServiceImpl(StudentRepository studentRepository, StudentResDTOMapper studentResDTOMapper,StudentReqDTOMapper studentReqDTOMapper) {
        this.studentRepository = studentRepository;
        this.studentResDTOMapper = studentResDTOMapper;
        this.studentReqDTOMapper = studentReqDTOMapper;
    }

    @Override
    public StudentResDTO createStudent(StudentReqDTO studentReqDTO) {
        // Map DTO to Entity
        Student student = studentReqDTOMapper.apply(studentReqDTO);
        return studentResDTOMapper.apply(studentRepository.save(student));
    }

    @Override
    public StudentResDTO getStudentById(int id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));
        return studentResDTOMapper.apply(student);
    }

    @Override
    public List<StudentResDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(studentResDTOMapper).collect(Collectors.toList());
    }

    @Override
    public StudentResDTO updateStudent(int id, StudentReqDTO studentReqDTO) {
        Student original = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));

        // Map DTO to entity
        Student student = studentReqDTOMapper.apply(studentReqDTO);

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
        return studentResDTOMapper.apply(updatedStudent);
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
