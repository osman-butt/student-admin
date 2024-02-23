package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.HouseDTO;
import edu.hogwarts.studentadmin.models.House;

import java.util.List;
import java.util.Optional;

public interface HouseService extends Service<HouseDTO, House> {
    List<HouseDTO> findAll();
    Optional<HouseDTO> findById(String name);
}
