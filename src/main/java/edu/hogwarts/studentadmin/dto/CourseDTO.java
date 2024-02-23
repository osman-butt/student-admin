package edu.hogwarts.studentadmin.dto;

import java.util.HashSet;
import java.util.Set;

public class CourseDTO {
    private final int id;
    private final String subject;
    private final int schoolYear;
    private final boolean current;
    private final TeacherDTO teacher;
    private final Set<StudentDTO> students;

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
