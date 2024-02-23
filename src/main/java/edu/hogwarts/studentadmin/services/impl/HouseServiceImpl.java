package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.HouseDTO;
import edu.hogwarts.studentadmin.models.ColorType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.HouseColor;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.services.HouseService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    public HouseServiceImpl(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Override
    public List<HouseDTO> findAll() {
        return houseRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public Optional<HouseDTO> findById(String name) {
        String nameAsTitle = name.substring(0, 1).toUpperCase() + name.substring(1);
        Optional<House> optionalHouse = houseRepository.findByName(nameAsTitle);
        return optionalHouse.map(this::toDTO);
    }

    @Override
    public House toEntity(HouseDTO dto) {
        return new House(dto.name(), dto.founder(), new HouseColor(ColorType.valueOf(dto.colors().get(0)),ColorType.valueOf(dto.colors().get(1))));
    }

    @Override
    public HouseDTO toDTO(House entity) {
        return new HouseDTO(
                entity.getName(),
                entity.getFounder(),
                Arrays.asList(entity.getColors().getPrimaryColor().toString(),entity.getColors().getSecondaryColor().toString()));
    }
}
