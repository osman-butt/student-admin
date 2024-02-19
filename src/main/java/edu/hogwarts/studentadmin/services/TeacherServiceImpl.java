package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService{
    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository,HouseRepository houseRepository) {
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }
    @Override
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Optional<Teacher> getTeacherById(int id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()) {
            return teacherOptional;
        } else {
            throw new NotFoundException("Unable to find teacher with id=" + id);
        }
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher updateTeacher(int id, Teacher teacher) {
        Optional<Teacher> original = teacherRepository.findById(id);
        if(original.isPresent()) {
            Teacher origTeacher = original.get();
            // Get house
            House newHouse = teacher.getHouse();
            Optional<House> house = houseRepository.findByName(newHouse.getName());
            if (house.isPresent()) {
                origTeacher.setHouse(teacher.getHouse());
            } else {
                throw new NotFoundException("Unable to find house named " + teacher.getHouse().getName());
            }
            // Update Teacher
            origTeacher.setFirstName(teacher.getFirstName());
            origTeacher.setMiddleName(teacher.getMiddleName());
            origTeacher.setLastName(teacher.getLastName());
            origTeacher.setEmployment(teacher.getEmployment());
            origTeacher.setDateOfBirth(teacher.getDateOfBirth());
            origTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            origTeacher.setEmploymentStart(teacher.getEmploymentStart());
            origTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            return teacherRepository.save(origTeacher);
        } else {
            throw new NotFoundException("Unable to find teacher with id=" + id);
        }
    }

    @Override
    public void deleteTeacher(int id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if( teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            // Remove student from courses
            teacher.getCourses().forEach(course -> course.setTeacher(null));
            // Delete the student entity
            teacherRepository.delete(teacher);
        } else {
            throw new NotFoundException("Unable to find teacher with id=" + id);
        }
    }
}
