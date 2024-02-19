package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentServiceImpl(StudentRepository studentRepository,HouseRepository houseRepository) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }
    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> getStudentById(int id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional;
        } else {
            throw new NotFoundException("Unable to find student with id=" + id);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(int id, Student student) {
        Optional<Student> original = studentRepository.findById(id);
        if (original.isPresent()) {
            Student origStudent = original.get();
            // Set house
            House newHouse = student.getHouse();
            Optional<House> house = houseRepository.findById(newHouse.getId());
            if (house.isPresent()) {
                house.ifPresent(origStudent::setHouse);
            } else {
                throw new NotFoundException("Unable to find house with id=" + student.getHouse().getId());
            }
            // Update student info
            origStudent.setFirstName(student.getFirstName());
            origStudent.setMiddleName(student.getMiddleName());
            origStudent.setLastName(student.getLastName());
            origStudent.setGraduated(student.isGraduated());
            origStudent.setDateOfBirth(student.getDateOfBirth());
            origStudent.setEnrollmentYear(student.getEnrollmentYear());
            origStudent.setPrefect(student.isPrefect());
            origStudent.setGraduationYear(student.getGraduationYear());
            return studentRepository.save(origStudent);
        } else {
            throw new NotFoundException("Unable to find student with id=" + id);
        }
    }

    @Override
    public void deleteStudent(int id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if( studentOptional.isPresent()) {
            Student student = studentOptional.get();
            // Remove student from courses
            student.getCourses().forEach(course -> course.getStudents().remove(student));
            // Delete the student entity
            studentRepository.delete(student);
        } else {
            throw new NotFoundException("Unable to find student with id=" + id);
        }
    }
}
