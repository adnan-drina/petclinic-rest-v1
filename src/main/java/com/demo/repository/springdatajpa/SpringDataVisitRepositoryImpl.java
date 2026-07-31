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
import com.demo.model.Visit;

/**
 * @author Vitaliy Fedoriv
 *
 */

@ApplicationScoped
@Transactional
public class SpringDataVisitRepositoryImpl implements VisitRepositoryOverride {
    
    private final EntityManager em;

    @Inject
    public SpringDataVisitRepositoryImpl(EntityManager em) {
        this.em = em;
    }

	@Override
	public void delete(Visit visit) {
		String visitId = visit.getId().toString();
		em.createQuery("DELETE FROM Visit visit WHERE id=" + visitId).executeUpdate();
        if (em.contains(visit)) {
            em.remove(visit);
        }
	}


}
