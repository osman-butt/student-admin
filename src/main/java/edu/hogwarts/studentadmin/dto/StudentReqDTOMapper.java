package edu.hogwarts.studentadmin.dto;

import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentReqDTOMapper implements Function<StudentReqDTO,Student> {
    @Autowired
    EntityManager entityManager;
    @Override
    public Student apply(StudentReqDTO studentReqDTO) {
        String[] nameParts = nameParts(studentReqDTO.name());
        House house = entityManager.find(House.class, studentReqDTO.house());
        return new Student(
                nameParts[0],
                nameParts[1],
                nameParts[2],
                studentReqDTO.dateOfBirth(),
                house,
                studentReqDTO.prefect(),
                studentReqDTO.enrollmentYear(),
                studentReqDTO.graduationYear(),
                studentReqDTO.graduated(),
                studentReqDTO.schoolYear()
        );
    }

    private String[] nameParts(String fullName) {
        String[] names = fullName.split("\\s+");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[names.length - 1] : null;
        String middleName = null;
        if (names.length > 2) {
            middleName = java.util.Arrays.stream(names, 1, names.length - 1)
                    .collect(Collectors.joining(" "));
        }
        return new String[]{firstName, middleName, lastName};
    }
}
