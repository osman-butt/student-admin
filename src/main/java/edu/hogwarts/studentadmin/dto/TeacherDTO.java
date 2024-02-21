package edu.hogwarts.studentadmin.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class TeacherDTO {
    private final int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private final LocalDate dateOfBirth;
    private final String house;

    private final boolean headOfHouse;
    private final String employment;
    private final LocalDate employmentStart;
    private final LocalDate employmentEnd;

    public TeacherDTO(int id, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String house, boolean headOfHouse, String employment, LocalDate employmentStart, LocalDate employmentEnd) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.house = house;
        this.headOfHouse = headOfHouse;
        this.employment = employment;
        this.employmentStart = employmentStart;
        this.employmentEnd = employmentEnd;
    }

    @JsonSetter("name")
    private void setNames(String name) {
        String[] names = nameParts(name);
        this.firstName = names[0];
        this.middleName = names[1];
        this.lastName = names[2];
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

    public boolean isHeadOfHouse() {
        return headOfHouse;
    }

    public String getEmployment() {
        return employment;
    }

    public LocalDate getEmploymentStart() {
        return employmentStart;
    }

    public LocalDate getEmploymentEnd() {
        return employmentEnd;
    }
}
