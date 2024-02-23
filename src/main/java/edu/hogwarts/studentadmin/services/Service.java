package edu.hogwarts.studentadmin.services;

import java.util.List;

/**
 * This interface defines a set of generic services for converting between DTOs (Data Transfer Objects)
 * and entities, as well as performing CRUD operations.
 *
 * @param <D> The DTO type.
 * @param <E> The entity type.
 */
public interface Service<D, E> {

    /**
     * Converts a DTO to its corresponding entity representation.
     *
     * @param dto The DTO to be converted.
     * @return The entity representation of the DTO.
     */
    E toEntity(D dto);

    /**
     * Converts an entity to its corresponding DTO representation.
     *
     * @param entity The entity to be converted.
     * @return The DTO representation of the entity.
     */
    D toDTO(E entity);

//    /**
//     * Retrieves a list of DTOs representing all entities.
//     *
//     * @param dto An instance of the DTO used for filtering or other purposes.
//     * @return A list of DTOs representing all entities.
//     */
//    List<D> findAll(D dto);
//
//    /**
//     * Retrieves a single DTO representing the entity with the specified ID.
//     *
//     * @param id The ID of the entity to retrieve.
//     * @return A DTO representing the entity with the specified ID.
//     */
//    D findById(int id);
//
//    /**
//     * Creates a new entity based on the provided DTO.
//     *
//     * @param dto The DTO containing the data for the new entity.
//     * @return A DTO representing the newly created entity.
//     */
//    D create(D dto);
//
//    /**
//     * Updates an existing entity based on the provided DTO.
//     *
//     * @param dto The DTO containing the updated data for the entity.
//     * @return A DTO representing the updated entity.
//     */
//    D update(D dto);
//
//    /**
//     * Deletes the entity with the specified ID.
//     *
//     * @param id The ID of the entity to delete.
//     * @return A DTO representing the deleted entity.
//     */
//    D delete(int id);
}
