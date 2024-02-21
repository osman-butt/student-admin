package edu.hogwarts.studentadmin.mapper.impl;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.EmpType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Teacher;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class TeacherDTOMapper implements DTOMapper<TeacherDTO, Teacher> {
    EntityManager entityManager;

    public TeacherDTOMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Teacher toEntity(TeacherDTO dto) {
        House house = entityManager.find(House.class, dto.getHouse());
        if(house == null) {
            throw new NotFoundException("House named "+dto.getHouse() + " not found");
        }
        return new Teacher(
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getLastName(),
                dto.getDateOfBirth(),
                house,
                dto.isHeadOfHouse(),
                EmpType.valueOf(dto.getEmployment().toUpperCase()),
                dto.getEmploymentStart(),
                dto.getEmploymentEnd()
        );
    }

    @Override
    public TeacherDTO toDTO(Teacher entity) {
        return new TeacherDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getHouse().getName(),
                entity.isHeadOfHouse(),
                entity.getEmployment().toString(),
                entity.getEmploymentStart(),
                entity.getEmploymentEnd()
        );
    }
}
