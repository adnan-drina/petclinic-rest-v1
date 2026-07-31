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
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Visit;
import java.util.List;

/**
 * @author Vitaliy Fedoriv
 *
 */

@ApplicationScoped
@Transactional
public class SpringDataPetTypeRepositoryImpl implements PetTypeRepositoryOverride {
    
    private final EntityManager em;

    @Inject
    public SpringDataPetTypeRepositoryImpl(EntityManager em) {
        this.em = em;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void delete(PetType petType) {
        em.remove(em.contains(petType) ? petType : em.merge(petType));
		Integer petTypeId = petType.getId();
		
		List<Pet> pets = em.createQuery("SELECT pet FROM Pet pet WHERE type_id=" + petTypeId).getResultList();
		for (Pet pet : pets){
			List<Visit> visits = pet.getVisits();
			for (Visit visit : visits){
				em.createQuery("DELETE FROM Visit visit WHERE id=" + visit.getId()).executeUpdate();
			}
			em.createQuery("DELETE FROM Pet pet WHERE id=" + pet.getId()).executeUpdate();
		}
		em.createQuery("DELETE FROM PetType pettype WHERE id=" + petTypeId).executeUpdate();
	}

}
