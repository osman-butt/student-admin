package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.models.EmpType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;

    public InitData(CourseRepository courseRepository,StudentRepository studentRepository,TeacherRepository teacherRepository, HouseRepository houseRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }

    public void run(String... args) {
        System.out.println("InitData is running");

        // Create houses
        House gryffindor = new House("Gryffindor","Godric Gryffindor");
        House hufflepuff = new House("Hufflepuff","Helga Hufflepuff");
        House ravenclaw = new House("Ravenclaw","Rowena Ravenclaw");
        House slytherin = new House("Slytherin","Salazar Slytherin");
        houseRepository.saveAll(List.of(gryffindor,hufflepuff,ravenclaw,slytherin));

        // Create students
        Student harry  = new Student("Harry","James","Potter", LocalDate.parse("1980-01-05"),gryffindor,false,1991,1999,false);
        studentRepository.saveAll(List.of(harry));

        // Create teachers
        Teacher snape = new Teacher("Severus",null,"Snape",LocalDate.parse("1946-06-29"),slytherin,true, EmpType.TENURED,LocalDate.parse("1980-01-01"),null);
        teacherRepository.saveAll(List.of(snape));

        // Create Course
    }
}
