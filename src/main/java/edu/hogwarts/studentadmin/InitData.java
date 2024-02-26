package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.models.*;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@Profile({"test", "dev"})
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
        System.out.println("Adding initial data to DB.");

        // Create houses
        House gryffindor = new House("Gryffindor","Godric Gryffindor",new HouseColor(ColorType.RED,ColorType.YELLOW));
        House hufflepuff = new House("Hufflepuff","Helga Hufflepuff",new HouseColor(ColorType.YELLOW,ColorType.BLACK));
        House ravenclaw = new House("Ravenclaw","Rowena Ravenclaw",new HouseColor(ColorType.BLUE,ColorType.BRONZE));
        House slytherin = new House("Slytherin","Salazar Slytherin",new HouseColor(ColorType.YELLOW,ColorType.BLACK));
        houseRepository.saveAll(Set.of(gryffindor,hufflepuff,ravenclaw,slytherin));

        // Create students
        Student harry  = new Student("Harry","James","Potter", LocalDate.parse("1980-01-05"),gryffindor,false,1991,null,false,1);
        Student ron = new Student("Ron",null,"Weasley",LocalDate.parse("1981-02-12"),gryffindor,false,1991,null,false,1);
        Student hermione = new Student("Hermione",null,"Granger",LocalDate.parse("1981-01-23"),gryffindor,true,1991,null,false,1);
        Student padma = new Student("Padma",null,"Patil",LocalDate.parse("1979-08-19"),ravenclaw,true,1991,null,false,1);
        Student draco = new Student("Draco",null,"Malfoy",LocalDate.parse("1979-09-11"),slytherin,true,1991,null,false,1);
        Student crabbe = new Student("Vincent",null,"Crabbe",LocalDate.parse("1980-03-21"),slytherin,false,1991,null,false,1);
        Student goyle = new Student("Gregory",null,"Goyle",LocalDate.parse("1981-06-22"),slytherin,false,1991,null,false,1);
        Student justin = new Student("Justin",null,"Finch-Fletchley",LocalDate.parse("1981-01-27"),hufflepuff,false,1991,null,false,1);
        Student seamus = new Student("Seamus",null,"Finnigan",LocalDate.parse("1979-11-01"),gryffindor,false,1991,null,false,1);
        Student neville = new Student("Neville",null,"Longbottom",LocalDate.parse("1980-01-09"),gryffindor,false,1991,null,false,1);
        Student parvati = new Student("Parvati",null,"Patil",LocalDate.parse("1980-06-06"),gryffindor,true,1991,null,false,1);
        Student pansy = new Student("Pansy",null,"Parkinson",LocalDate.parse("1980-07-04"),slytherin,true,1991,null,false,1);

        studentRepository.saveAll(Set.of(harry,ron,hermione,padma,draco,crabbe,goyle,justin,seamus,neville,parvati,pansy));

        // Create teachers
        Teacher snape = new Teacher("Severus",null,"Snape",LocalDate.parse("1946-06-29"),slytherin,true, EmpType.TENURED,LocalDate.parse("1980-01-01"),null);
        teacherRepository.saveAll(Set.of(snape));

        // Create Course
        Course potions = new Course();
        potions.setSubject("Potions");
        potions.setCurrent(true);
        potions.setTeacher(snape);
        potions.setSchoolYear(1);
        potions.setStudents(Set.of(harry,ron,hermione,padma,draco,crabbe,goyle,justin,seamus,neville));
        Course darkArts = new Course();
        darkArts.setSubject("Dark arts");
        darkArts.setCurrent(true);
        darkArts.setTeacher(snape);
        darkArts.setSchoolYear(1);
        darkArts.setStudents(Set.of(harry,ron,hermione));
        courseRepository.saveAll(Set.of(potions,darkArts));
    }
}
