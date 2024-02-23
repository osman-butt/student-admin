package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.dto.HouseDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.services.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public ResponseEntity<List<HouseDTO>> getAllHouses() {
        return ResponseEntity.ok(houseService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<HouseDTO> getHouseByName(@PathVariable String name) {
        return ResponseEntity.ok(houseService.findById(name).orElseThrow(() -> new NotFoundException("Unable to find house with name " + name)));
    }
}
