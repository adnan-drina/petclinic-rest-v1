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
package com.demo.repository.jpa;

import jakarta.transaction.Transactional;

import jakarta.inject.Inject;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceException;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.repository.PetRepository;

/**
 * JPA implementation of the {@link PetRepository} interface.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
@Transactional
public class JpaPetRepositoryImpl implements PetRepository {

    private final EntityManager em;

    @Inject
    public JpaPetRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<PetType> findPetTypes() {
        return this.em.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name").getResultList();
    }

    @Override
    public Pet findById(int id) {
        return this.em.find(Pet.class, id);
    }

    @Override
    @Transactional
    public void save(Pet pet) {
        if (pet.getId() == null) {
            this.em.persist(pet);
        } else {
            this.em.merge(pet);
        }
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pet> findAll() throws PersistenceException {
		return this.em.createQuery("SELECT pet FROM Pet pet").getResultList();
	}

	@Override
	public void delete(Pet pet) throws PersistenceException {
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet_id=" + petId).executeUpdate();
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
		if (em.contains(pet)) {
			em.remove(pet);
		}
	}

}
