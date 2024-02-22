package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.HouseDTO;
import edu.hogwarts.studentadmin.dto.HouseDTOMapper;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.services.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseDTOMapper houseDTOMapper;

    public HouseServiceImpl(HouseRepository houseRepository,HouseDTOMapper houseDTOMapper) {
        this.houseRepository = houseRepository;
        this.houseDTOMapper = houseDTOMapper;
    }

    @Override
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream().map(houseDTOMapper).toList();
    }

    @Override
    public HouseDTO getHouseByName(String name) {
        String nameToTitle = name.substring(0, 1).toUpperCase() + name.substring(1);
        Optional<HouseDTO> houseOptional = houseRepository.findByName(nameToTitle).stream().map(houseDTOMapper).findFirst();
        if (houseOptional.isPresent()) {
            return houseOptional.get();
        } else {
            throw new NotFoundException("Unable to find House named "+name);
        }
    }
}
