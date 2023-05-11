package org.cyberark.conjur.demos.controller;

import java.net.URI;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cyberark.conjur.demos.model.Pet;
import org.cyberark.conjur.demos.repository.PetRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/pet")
@Tag(name = "Pets")
class PetController {
	private final PetRepository repository;

	private PetController(PetRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/{id}")
	Pet getPet(@PathVariable Long id) {
		return repository.findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping()
	ResponseEntity<?> createPet(@RequestBody Pet pet) {
		if (pet == null) {
			return ResponseEntity.badRequest().body(null);
		}

		Pet result = repository.save(new Pet(pet.getName()));
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> deletePet(@PathVariable Long id) {
		Pet pet = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (pet == null) {
			return ResponseEntity.notFound().build();
		}
		repository.delete(pet);
		return ResponseEntity.ok().build();
	}
}