package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.services.HouseService;
import edu.hogwarts.studentadmin.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final HouseService houseService;

    public StudentServiceImpl(StudentRepository studentRepository,HouseService houseService) {
        this.studentRepository = studentRepository;
        this.houseService = houseService;
    }

    @Override
    public StudentDTO create(StudentDTO studentDTO) {
        return toDTO(studentRepository.save(toEntity(studentDTO)));
    }

    @Override
    public Optional<StudentDTO> findById(int id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.map(this::toDTO);
    }

    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDTO update(int id, StudentDTO studentDTO) {
        Student original = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id));

        // Map DTO to entity
        Student student = toEntity(studentDTO);

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
        return toDTO(updatedStudent);
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

    private Optional<StudentDTO> findOneByFullName(String firstName, String middleName, String lastName) {
        List<Student> students = studentRepository.findByFullName(firstName,middleName,lastName);
        if (students.size() == 1) {
            return Optional.of(toDTO(students.get(0)));
        } else {
            return Optional.of(null);
        }
    }

    @Override
    public Optional<StudentDTO> findOneByIdOrFullName(StudentDTO studentDTO) {
        if(studentDTO.getId() != 0) {
            Optional<Student> studentOptional = studentRepository.findById(studentDTO.getId());
            return studentOptional.map(this::toDTO);
        } else if (studentDTO.getFirstName() != null) {
            return findOneByFullName(studentDTO.getFirstName(),studentDTO.getMiddleName(),studentDTO.getLastName());
        } else {
            return Optional.of(null);
        }
    }

    @Override
    public Student toEntity(StudentDTO dto) {
        House house = dto.getHouse() == null ? null : houseService.findById(dto.getHouse()).map(houseService::toEntity).orElse(null);
        return new Student(
                dto.getId(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getLastName(),
                dto.getDateOfBirth(),
                house,
                dto.isPrefect(),
                dto.getEnrollmentYear(),
                dto.getGraduationYear(),
                dto.isGraduated(),
                dto.getSchoolYear()
        );
    }

    @Override
    public StudentDTO toDTO(Student entity) {
        return new StudentDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getHouse().getName(),
                entity.isPrefect(),
                entity.getEnrollmentYear(),
                entity.getGraduationYear(),
                entity.isGraduated(),
                entity.getSchoolYear());
    }
}
