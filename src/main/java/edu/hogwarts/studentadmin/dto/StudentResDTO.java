package edu.hogwarts.studentadmin.dto;

import java.time.LocalDate;

public record StudentResDTO(
        int id,
        String firstName,
        String middleName,
        String lastName,
        LocalDate dateOfBirth,
        String house,
        boolean prefect,
        int enrollmentYear,
        Integer graduationYear,
        boolean graduated,
        int schoolYear
) {
}