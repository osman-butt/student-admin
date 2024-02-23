package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.services.CourseService;
import edu.hogwarts.studentadmin.services.StudentService;
import edu.hogwarts.studentadmin.services.TeacherService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public CourseServiceImpl(CourseRepository courseRepository, TeacherService teacherService, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CourseDTO> findById(int id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(this::toDTO);
    }

    @Override
    public Teacher findTeacherById(int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            return course.getTeacher();
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public Set<Student> findStudentsById(int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            return course.getStudents();
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        Course course = toEntity(courseDTO);
        return toDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO update(int id, CourseDTO courseDTO) {
        Course origCourse = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        Course course = toEntity(courseDTO);
        // Update course students
        Set<Student> updatedStudents = new HashSet<>();
        for (Student student : course.getStudents()) {
            Student foundStudent = studentService.toEntity(studentService.findById(student.getId())
                    .orElseThrow(() -> new NotFoundException("Unable to find course with id=" + student.getId())));
            updatedStudents.add(foundStudent);
        }
        origCourse.setStudents(updatedStudents);
        // Update course teacher
        Teacher newTeacher = course.getTeacher();
        Teacher teacher = teacherService.toEntity(teacherService.findById(newTeacher.getId())
                .orElseThrow(() -> new NotFoundException("Unable to find course with id=" + newTeacher.getId())));
        origCourse.setTeacher(teacher);
        // Update course info
        origCourse.setSubject(course.getSubject());
        origCourse.setCurrent(course.isCurrent());
        origCourse.setSchoolYear(course.getSchoolYear());
        return toDTO(courseRepository.save(origCourse));

    }

    @Override
    public CourseDTO updateTeacher(int id, int teacherId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Update teacher
        Teacher teacher = teacherService.toEntity(teacherService.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Unable to find course with id=" + teacherId)));
        course.setTeacher(teacher);
        return toDTO(courseRepository.save(course));

    }

    @Override
    public CourseDTO addStudent(int id, int studentId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Check if student is already enrolled
        if(isStudentEnrolled(course.getStudents(),studentId)) {
            return toDTO(course);
        } else {
            Set<Student> newStudentList = course.getStudents();
            Student student = studentService.toEntity(
                    studentService.findById(studentId)
                            .orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id))
            );
            newStudentList.add(student);
            course.setStudents(newStudentList);
            return toDTO(courseRepository.save(course));
        }
    }

    @Override
    public CourseDTO addStudents(int id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Todo
        // Get  students
        int addedStudents = 0;
        Set<Student> students = toEntity(courseDTO).getStudents();
        for (Student s : students) {
            System.out.println(s.getId());
        }
        for (Student student : students) {
            Student matchedStudent;
            if (student.getId() == 0) {
                List<StudentDTO> studentDTO = studentService.findByFullName(student.getFirstName(),student.getMiddleName(),student.getLastName());
                if(studentDTO.size() == 1) {
                    matchedStudent = studentService.toEntity(studentDTO.get(0));
                } else {
                    throw new NotFoundException("Unable to uniquely identify students. Use id or provide the fullname");
                }
            } else {
                matchedStudent = studentService.toEntity(
                        studentService.findById(student.getId())
                                .orElseThrow(() -> new NotFoundException("Unable to find course with id=" + student.getId()))
                );
            }
            // Is student eligible for course (same schoolYear)
            if(matchedStudent.getSchoolYear() != course.getSchoolYear()) {
                // Todo
                // Add proper error handling
                throw new NotFoundException("Could not add students. Some of the students does not have the same school year as course");
            }
            // If not enrolled, add student to course
            if(!isStudentEnrolled(course.getStudents(),matchedStudent.getId())) {
                course.addStudent(matchedStudent);
                ++addedStudents;
            }
        }
        if (addedStudents>0) {
            Course updatedCourse = courseRepository.save(course);
            return toDTO(updatedCourse);
        } else {
            // Todo
            // Add proper error handling
            throw new NotFoundException("All students are already enrolled.");
        }
    }

    @Override
    public void delete(int id) {
        courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDTO deleteTeacher(int id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        course.setTeacher(null);
        return toDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO deleteStudent(int id, int studentId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Check if student is enrolled
        if(isStudentEnrolled(course.getStudents(),studentId)) {
            Set<Student> newStudentList = course.getStudents().stream()
                    .filter(student -> student.getId() != studentId)
                    .collect(Collectors.toSet());
            course.setStudents(newStudentList);
            return toDTO(courseRepository.save(course));
        } else {
            throw new NotFoundException("Student with id=" + studentId + " is not enrolled.");
        }
    }

    private boolean isStudentEnrolled(Set<Student> students, int id) {
        return students.stream().anyMatch(student -> student.getId() == id);
    }


    @Override
    public Course toEntity(CourseDTO dto) {
        return new Course(
                dto.getId(),
                dto.getSubject(),
                dto.getSchoolYear(),
                dto.isCurrent(),
                dto.getTeacher() == null ? null : teacherService.toEntity(dto.getTeacher()),
                dto.getStudents() == null ? null : dto.getStudents().stream().map(studentService::toEntity).collect(Collectors.toSet())
        );
    }

    @Override
    public CourseDTO toDTO(Course entity) {
        return new CourseDTO(
                entity.getId(),
                entity.getSubject(),
                entity.getSchoolYear(),
                entity.isCurrent(),
                entity.getTeacher() == null ? null : teacherService.toDTO(entity.getTeacher()),
                entity.getStudents() == null ? null : entity.getStudents().stream().map(studentService::toDTO).collect(Collectors.toSet())
        );
    }
}
