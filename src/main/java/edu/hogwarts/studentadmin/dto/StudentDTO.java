package edu.hogwarts.studentadmin.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class StudentDTO {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private final LocalDate dateOfBirth;
    private final String house;
    private final boolean prefect;
    private final int enrollmentYear;
    private final Integer graduationYear;
    private final boolean graduated;
    private final int schoolYear;

    public StudentDTO(int id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String house, boolean prefect, int enrollmentYear, Integer graduationYear, boolean graduated, int schoolYear) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.house = house;
        this.prefect = prefect;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
        this.graduated = graduated;
        this.schoolYear = schoolYear;
    }

    @JsonSetter("name")
    private void setNames(String name) {
        String[] names = nameParts(name);
        this.firstName = names[0];
        this.middleName = names[1];
        this.lastName = names[2];
    }

    public void setId(int id) {
        this.id = id;
    }

    private String[] nameParts(String fullName) {
        String[] names = fullName.split("\\s+");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[names.length - 1] : null;
        String middleName = null;
        if (names.length > 2) {
            middleName = java.util.Arrays.stream(names, 1, names.length - 1)
                    .collect(Collectors.joining(" "));
        }
        return new String[]{firstName, middleName, lastName};
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getHouse() {
        return house;
    }

    public boolean isPrefect() {
        return prefect;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public int getSchoolYear() {
        return schoolYear;
    }
}
