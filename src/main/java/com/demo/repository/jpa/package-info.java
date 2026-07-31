/**
 * The classes in this package represent the JPA implementation
 * of PetClinic's persistence layer.
 * 
 * <p>This package contains Data Access Object (DAO) implementations that use
 * Jakarta Persistence API (JPA) 3.0 for database operations. These implementations
 * provide an abstraction over direct JDBC code and leverage the full power of
 * object-relational mapping.
 * 
 * <p>JPA operations utilize:
 * <ul>
 * <li>{@code jakarta.persistence.EntityManager} - for CRUD operations</li>
 * <li>{@code jakarta.persistence.NamedQuery} - for typed queries</li>
 * <li>{@code jakarta.persistence.TypedQuery} - for type-safe query execution</li>
 * <li>JPQL (Jakarta Persistence Query Language) for database-agnostic queries</li>
 * <li>Transaction management via {@code jakarta.persistence.EntityTransaction}</li>
 * </ul>
 * 
 * <p>All JPA repositories in this package work with entities defined in
 * {@code com.demo.model} package using Jakarta Persistence 3.0 annotations.
 * 
 * @since Jakarta EE 9 / Jakarta Persistence 3.0
 * @see com.demo.repository.jdbc for JDBC-based implementations
 */
package com.demo.repository.jpa;

