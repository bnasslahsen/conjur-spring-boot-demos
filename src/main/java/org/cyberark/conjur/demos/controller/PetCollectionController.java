package org.cyberark.conjur.demos.controller;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cyberark.conjur.demos.model.Pet;
import org.cyberark.conjur.demos.repository.PetRepository;

import org.springframework.http.ResponseEntity;
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
	ResponseEntity<List<Pet>> getPets() {
		final List<Pet> petList = new ArrayList<>();
		final Iterable<Pet> all = repository.findAll();
		all.forEach(petList::add);
		return ResponseEntity.ok().body(petList);
	}
}