/*
 * Copyright 2016-2017 the original author or authors.
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

package com.demo.repository.springdatajpa;

import jakarta.persistence.EntityManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.demo.model.Pet;

/**
 * @author Vitaliy Fedoriv
 *
 */

@ApplicationScoped
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {
	
    private final EntityManager em;

    @Inject
    public SpringDataPetRepositoryImpl(EntityManager em) {
        this.em = em;
    }

	@Override
	public void delete(Pet pet) {
		String petId = pet.getId().toString();
		em.createQuery("DELETE FROM Visit visit WHERE pet_id=" + petId).executeUpdate();
		em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
        if (em.contains(pet)) {
            em.remove(pet);
        }
	}

}
