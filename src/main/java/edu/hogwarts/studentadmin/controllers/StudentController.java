package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentController(StudentRepository studentRepository,HouseRepository houseRepository) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id){
        Optional<Student> student = studentRepository.findById(id);
        return ResponseEntity.of(student);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        Optional<Student> original = studentRepository.findById(id);
        if (original.isPresent()) {
            Student origStudent = original.get();
            // Get house
            House newHouse = student.getHouse();
            Optional<House> house = houseRepository.findById(newHouse.getId());
            house.ifPresent(origStudent::setHouse);
            // Update student
            origStudent.setFirstName(student.getFirstName());
            origStudent.setMiddleName(student.getMiddleName());
            origStudent.setLastName(student.getLastName());
            origStudent.setGraduated(student.isGraduated());
            origStudent.setDateOfBirth(student.getDateOfBirth());
            origStudent.setEnrollmentYear(student.getEnrollmentYear());
            origStudent.setPrefect(student.isPrefect());
            origStudent.setGraduationYear(student.getGraduationYear());
            Student updatedStudent = studentRepository.save(origStudent);
            return ResponseEntity.ok().body(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deletePerson(@PathVariable int id) {
        Optional<Student> student = studentRepository.findById(id);
        studentRepository.deleteById(id);
        return ResponseEntity.of(student);
    }
}
