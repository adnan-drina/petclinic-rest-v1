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
import com.demo.model.Specialty;

/**
 * @author Vitaliy Fedoriv
 *
 */

@ApplicationScoped
public class SpringDataSpecialtyRepositoryImpl implements SpecialtyRepositoryOverride {
    
    private final EntityManager em;

    @Inject
    public SpringDataSpecialtyRepositoryImpl(EntityManager em) {
        this.em = em;
    }

	@Override
	public void delete(Specialty specialty) {
        em.remove(em.contains(specialty) ? specialty : em.merge(specialty));
		Integer specId = specialty.getId();
		em.createNativeQuery("DELETE FROM vet_specialties WHERE specialty_id=" + specId).executeUpdate();
		em.createQuery("DELETE FROM Specialty specialty WHERE id=" + specId).executeUpdate();
	}

}
