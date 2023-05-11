package org.cyberark.conjur.demos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cyberark.conjur.demos.model.Pet;
import org.cyberark.conjur.demos.repository.PetRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pets")
@Tag(name = "Pets")
class PetCollectionController {

	private final PetRepository repository;

	private PetCollectionController(PetRepository repository) {
		this.repository = repository;
	}

	@GetMapping()
	Iterable<Pet> getPets() {
		return repository.findAll();
	}
}