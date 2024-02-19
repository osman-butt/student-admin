package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.HouseDTO;

import java.util.List;

public interface HouseService {
    List<HouseDTO> getAllHouses();
    HouseDTO getHouseByName(String name);
}
