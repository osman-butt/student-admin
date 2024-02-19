package edu.hogwarts.studentadmin.dto;


import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

@Service
public class HouseDTOMapper implements Function<House,HouseDTO> {
    @Override
    public HouseDTO apply(House house) {
        return new HouseDTO(
                house.getName(),
                house.getFounder(),
                Arrays.asList(house.getColors().getPrimaryColor().toString(),house.getColors().getSecondaryColor().toString()));
    }
}