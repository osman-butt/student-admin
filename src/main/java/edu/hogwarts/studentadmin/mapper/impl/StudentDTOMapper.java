package edu.hogwarts.studentadmin.mapper.impl;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.mapper.DTOMapper;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class StudentDTOMapper implements DTOMapper<StudentDTO, Student> {

    EntityManager entityManager;

    public StudentDTOMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Student toEntity(StudentDTO dto) {
        House house = entityManager.find(House.class, dto.getHouse());
        if(house == null) {
            throw new NotFoundException("House named "+dto.getHouse() + " not found");
        }
        return new Student(
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getLastName(),
                dto.getDateOfBirth(),
                house,
                dto.isPrefect(),
                dto.getEnrollmentYear(),
                dto.getGraduationYear(),
                dto.isGraduated(),
                dto.getSchoolYear()
        );
    }

    @Override
    public StudentDTO toDTO(Student entity) {
        return new StudentDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getHouse().getName(),
                entity.isPrefect(),
                entity.getEnrollmentYear(),
                entity.getGraduationYear(),
                entity.isGraduated(),
                entity.getSchoolYear());
    }
}
