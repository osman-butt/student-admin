package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import edu.hogwarts.studentadmin.services.CourseService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DTOMapper<CourseDTO,Course> courseDTOMapper;

    public CourseServiceImpl(CourseRepository courseRepository,StudentRepository studentRepository,TeacherRepository teacherRepository,DTOMapper<CourseDTO,Course> courseDTOMapper) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseDTOMapper = courseDTOMapper;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(courseDTOMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(int id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        return courseDTOMapper.toDTO(course);
    }

    @Override
    public Teacher getTeacherFromCourse(int id) {
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
    public Set<Student> getCourseStudentsById(int id) {
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
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseDTOMapper.toEntity(courseDTO);
        return courseDTOMapper.toDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO updateCourse(int id, CourseDTO courseDTO) {
        Course origCourse = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        Course course = courseDTOMapper.toEntity(courseDTO);
        // Update course students
        Set<Student> updatedStudents = new HashSet<>();
        for (Student student : course.getStudents()) {
            Optional<Student> foundStudent = studentRepository.findById(student.getId());
            foundStudent.ifPresent(updatedStudents::add);
        }
        origCourse.setStudents(updatedStudents);
        // Update course teacher
        Teacher newTeacher = course.getTeacher();
        Optional<Teacher> teacher = teacherRepository.findById(newTeacher.getId());
        teacher.ifPresent(origCourse::setTeacher);
        // Update course info
        origCourse.setSubject(course.getSubject());
        origCourse.setCurrent(course.isCurrent());
        origCourse.setSchoolYear(course.getSchoolYear());
        return courseDTOMapper.toDTO(courseRepository.save(origCourse));

    }

    @Override
    public CourseDTO updateCourseTeacher(int id, int teacherId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Update teacher
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        course.setTeacher(teacher);
        return courseDTOMapper.toDTO(courseRepository.save(course));

    }

    @Override
    public CourseDTO addStudentToCourse(int id, int studentId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Check if student is already enrolled
        if(isStudentEnrolled(course.getStudents(),studentId)) {
            return courseDTOMapper.toDTO(course);
        } else {
            Set<Student> newStudentList = course.getStudents();
            Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
            newStudentList.add(student);
            course.setStudents(newStudentList);
            return courseDTOMapper.toDTO(courseRepository.save(course));
        }
    }

    @Override
    public void deleteCourse(int id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDTO removeTeacherFromCourse(int id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        course.setTeacher(null);
        return courseDTOMapper.toDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO removeStudentFromCourse(int id, int studentId) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find course with id=" + id));
        // Check if student is enrolled
        if(isStudentEnrolled(course.getStudents(),studentId)) {
            Set<Student> newStudentList = course.getStudents().stream()
                    .filter(student -> student.getId() != studentId)
                    .collect(Collectors.toSet());
            course.setStudents(newStudentList);
            return courseDTOMapper.toDTO(courseRepository.save(course));
        } else {
            throw new NotFoundException("Student with id=" + studentId + " is not enrolled.");
        }
    }

    private boolean isStudentEnrolled(Set<Student> students, int id) {
        return students.stream().anyMatch(student -> student.getId() == id);
    }
}
