package edu.hogwarts.studentadmin.dto;

import java.util.HashSet;
import java.util.Set;

public class CourseDTO {
    private int id;
    private String subject;
    private int schoolYear;
    private boolean current;
    private TeacherDTO teacher;
    private Set<StudentDTO> students = new HashSet<>();

    public CourseDTO(int id, String subject, int schoolYear, boolean current, TeacherDTO teacher, Set<StudentDTO> students) {
        this.id = id;
        this.subject = subject;
        this.schoolYear = schoolYear;
        this.current = current;
        this.teacher = teacher;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public boolean isCurrent() {
        return current;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public Set<StudentDTO> getStudents() {
        return students;
    }
}
