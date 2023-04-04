package org.cyberark.conjur.demos.repository;


import org.cyberark.conjur.demos.model.Pet;

import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, Long> {
}