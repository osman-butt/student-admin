package edu.hogwarts.studentadmin.dto;

import java.util.List;

public record HouseDTO(
        String name,
        String founder,
        List<String> colors
) {
}
