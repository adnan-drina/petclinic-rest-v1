/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.service;

import java.util.Collection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.repository.OwnerRepository;
import com.demo.repository.PetRepository;
import com.demo.repository.PetTypeRepository;
import com.demo.repository.SpecialtyRepository;
import com.demo.repository.VetRepository;
import com.demo.repository.VisitRepository;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped

public class ClinicServiceImpl implements ClinicService {

    private PetRepository petRepository;
    private VetRepository vetRepository;
    private OwnerRepository ownerRepository;
    private VisitRepository visitRepository;
    private SpecialtyRepository specialtyRepository;
	private PetTypeRepository petTypeRepository;

    @Inject
     public ClinicServiceImpl(
      		 PetRepository petRepository,
 			 VetRepository vetRepository,
 			 OwnerRepository ownerRepository,
 			 VisitRepository visitRepository,
 			 SpecialtyRepository specialtyRepository,
			 PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository; 
		this.petTypeRepository = petTypeRepository;
    }

	@Override
	public Collection<Pet> findAllPets() throws PersistenceException {
		return petRepository.findAll();
	}

	@Override
	public void deletePet(Pet pet) throws PersistenceException {
		petRepository.delete(pet);
	}

	@Override
	public Visit findVisitById(int visitId) throws PersistenceException {
		return visitRepository.findById(visitId);
	}

	@Override
	public Collection<Visit> findAllVisits() throws PersistenceException {
		return visitRepository.findAll();
	}

	@Override
	public void deleteVisit(Visit visit) throws PersistenceException {
		visitRepository.delete(visit);
	}

	@Override
	public Vet findVetById(int id) throws PersistenceException {
		Vet vet = null;
		try {
			vet = vetRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return vet;
	}

	@Override
	public Collection<Vet> findAllVets() throws PersistenceException {
		return vetRepository.findAll();
	}

	@Override
	public void saveVet(Vet vet) throws PersistenceException {
		vetRepository.save(vet);
	}

	@Override
	public void deleteVet(Vet vet) throws PersistenceException {
		vetRepository.delete(vet);
	}

	@Override
	public Collection<Owner> findAllOwners() throws PersistenceException {
		return ownerRepository.findAll();
	}

	@Override
	public void deleteOwner(Owner owner) throws PersistenceException {
		ownerRepository.delete(owner);
	}

	@Override
	public PetType findPetTypeById(int petTypeId) {
		PetType petType = null;
		try {
			petType = petTypeRepository.findById(petTypeId);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return petType;
	}

	@Override
	public Collection<PetType> findAllPetTypes() throws PersistenceException {
		return petTypeRepository.findAll();
	}

	@Override
	public void savePetType(PetType petType) throws PersistenceException {
		petTypeRepository.save(petType);
	}

	@Override
	public void deletePetType(PetType petType) throws PersistenceException {
		petTypeRepository.delete(petType);
	}

	@Override
	public Specialty findSpecialtyById(int specialtyId) {
		Specialty specialty = null;
		try {
			specialty = specialtyRepository.findById(specialtyId);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return specialty;
	}

	@Override
	public Collection<Specialty> findAllSpecialties() throws PersistenceException {
		return specialtyRepository.findAll();
	}

	@Override
	public void saveSpecialty(Specialty specialty) throws PersistenceException {
		specialtyRepository.save(specialty);
	}

	@Override
	public void deleteSpecialty(Specialty specialty) throws PersistenceException {
		specialtyRepository.delete(specialty);
	}

	@Override
	public Collection<PetType> findPetTypes() throws PersistenceException {
		return petRepository.findPetTypes();
	}

	@Override
	public Owner findOwnerById(int id) throws PersistenceException {
		Owner owner = null;
		try {
			owner = ownerRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return owner;
	}

	@Override
	public Pet findPetById(int id) throws PersistenceException {
		Pet pet = null;
		try {
			pet = petRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return pet;
	}

	@Override
	public void savePet(Pet pet) throws PersistenceException {
		petRepository.save(pet);
		
	}

	@Override
	public void saveVisit(Visit visit) throws PersistenceException {
		visitRepository.save(visit);
		
	}

	@Override
	public Collection<Vet> findVets() throws PersistenceException {
		return vetRepository.findAll();
	}

	@Override
	public void saveOwner(Owner owner) throws PersistenceException {
		ownerRepository.save(owner);
		
	}

	@Override
	public Collection<Owner> findOwnerByLastName(String lastName) throws PersistenceException {
		return ownerRepository.findByLastName(lastName);
	}

	@Override
	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
	


}