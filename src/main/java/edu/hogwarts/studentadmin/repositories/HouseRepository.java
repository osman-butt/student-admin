package edu.hogwarts.studentadmin.repositories;

import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House,Integer> {
}
