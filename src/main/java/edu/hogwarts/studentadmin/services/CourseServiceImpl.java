package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public CourseServiceImpl(CourseRepository courseRepository,StudentRepository studentRepository,TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getCourseById(int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            return courseOptional;
        } else {
            throw new NotFoundException("Unable to find course with id=" + id);
        }

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
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(int id, Course course) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course origCourse = courseOptional.get();
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
            return courseRepository.save(origCourse);
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public Course updateCourseTeacher(int id, int teacherId) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Update teacher
            Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
            if(optionalTeacher.isPresent()) {
                course.setTeacher(optionalTeacher.get());
                return courseRepository.save(course);
            } else {
                // Handle case where teacher is not found
                throw new NotFoundException("Unable to find teacher with id=" + teacherId);
            }
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public Course addStudentToCourse(int id, int studentId) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if(courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Check if student is already enrolled
            if(isStudentPresent(course.getStudents(),studentId)) {
                return course;
            } else {
                Set<Student> newStudentList = course.getStudents();
                Optional<Student> foundStudent = studentRepository.findById(studentId);
                if(foundStudent.isPresent()) {
                    newStudentList.add(foundStudent.get());
                    course.setStudents(newStudentList);
                    return courseRepository.save(course);
                } else {
                    // Handle case where student is not found
                    throw new NotFoundException("Unable to find student with id=" + studentId);
                }
            }
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public void deleteCourse(int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            courseRepository.deleteById(id);
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public Course removeTeacherFromCourse(int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setTeacher(null);
            return courseRepository.save(course);
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    @Override
    public Course removeStudentFromCourse(int id, int studentId) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Check if student is enrolled
            if(isStudentPresent(course.getStudents(),studentId)) {
                Set<Student> newStudentList = course.getStudents().stream()
                        .filter(student -> student.getId() != studentId)
                        .collect(Collectors.toSet());
                course.setStudents(newStudentList);
            } else {
                throw new NotFoundException("Student with id=" + studentId + " is not enrolled.");
            }
            return courseRepository.save(course);
        } else {
            // Handle case where course is not found
            throw new NotFoundException("Unable to find course with id=" + id);
        }
    }

    // Check if student is already enrolled in course
    private boolean isStudentPresent(Set<Student> students, int id) {
        return students.stream().anyMatch(student -> student.getId() == id);
    }
}
