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
import jakarta.persistence.Query;

import jakarta.persistence.PersistenceException;
import com.demo.model.Visit;
import com.demo.repository.VisitRepository;

/**
 * JPA implementation of the ClinicService interface using EntityManager.
 * <p/>
 * <p>The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
@Transactional
public class JpaVisitRepositoryImpl implements VisitRepository {

    private final EntityManager em;

    @Inject
    public JpaVisitRepositoryImpl(EntityManager em) {
        this.em = em;
    }



    @Override
    public void save(Visit visit) {
        if (visit.getId() == null) {
            this.em.persist(visit);
        } else {
            this.em.merge(visit);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Visit> findByPetId(Integer petId) {
        Query query = this.em.createQuery("SELECT v FROM Visit v where v.pet.id= :id");
        query.setParameter("id", petId);
        return query.getResultList();
    }
    
	@Override
	public Visit findById(int id) throws PersistenceException {
		return this.em.find(Visit.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Visit> findAll() throws PersistenceException {
        return this.em.createQuery("SELECT v FROM Visit v").getResultList();
	}

	@Override
	public void delete(Visit visit) throws PersistenceException {
        this.em.remove(this.em.contains(visit) ? visit : this.em.merge(visit));
	}

}
