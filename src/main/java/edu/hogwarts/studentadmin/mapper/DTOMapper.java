package edu.hogwarts.studentadmin.mapper;

public interface DTOMapper<R, E> {
    E toEntity(R dto);
    R toDTO(E entity);
}
