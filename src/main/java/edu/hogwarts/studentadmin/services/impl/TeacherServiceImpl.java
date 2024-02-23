package edu.hogwarts.studentadmin.services.impl;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.EmpType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import edu.hogwarts.studentadmin.services.HouseService;
import edu.hogwarts.studentadmin.services.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final HouseService houseService;

    public TeacherServiceImpl(TeacherRepository teacherRepository, HouseService houseService) {
        this.teacherRepository = teacherRepository;
        this.houseService = houseService;
    }
    @Override
    public TeacherDTO create(TeacherDTO teacherDTO) {
        return toDTO(teacherRepository.save(toEntity(teacherDTO)));
    }

    @Override
    public Optional<TeacherDTO> findById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        return optionalTeacher.map(this::toDTO);
    }

    @Override
    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public TeacherDTO update(int id, TeacherDTO teacherDTO) {
        Teacher original = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        Teacher teacher = toEntity(teacherDTO);

        // Update Teacher
        original.setFirstName(teacher.getFirstName());
        original.setMiddleName(teacher.getMiddleName());
        original.setLastName(teacher.getLastName());
        original.setHouse(teacher.getHouse());
        original.setEmployment(teacher.getEmployment());
        original.setDateOfBirth(teacher.getDateOfBirth());
        original.setEmploymentEnd(teacher.getEmploymentEnd());
        original.setEmploymentStart(teacher.getEmploymentStart());
        original.setHeadOfHouse(teacher.isHeadOfHouse());

        Teacher updatedTeacher = teacherRepository.save(original);
        return toDTO(updatedTeacher);
    }

    @Override
    public void delete(int id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id));
        // Remove teacher from courses
        teacher.getCourses().forEach(course -> course.setTeacher(null));
        // Delete the student entity
        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher toEntity(TeacherDTO dto) {
        House house = houseService.findById(dto.getHouse()).map(houseService::toEntity).orElse(null);
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
