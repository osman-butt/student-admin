package edu.hogwarts.studentadmin.dto;

import java.time.LocalDate;

public record StudentReqDTO(
        String name,
        LocalDate dateOfBirth,
        String house,
        boolean prefect,
        int enrollmentYear,
        Integer graduationYear,
        boolean graduated,
        int schoolYear
) {
}
