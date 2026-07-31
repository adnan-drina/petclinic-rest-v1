/**
 * The classes in this package represent the JDBC implementation
 * of PetClinic's persistence layer.
 * 
 * <p>This package contains Data Access Object (DAO) implementations that use
 * plain JDBC for database operations. While modern PetClinic applications use
 * Jakarta Persistence (JPA), these JDBC implementations are maintained for
 * reference and migration purposes.
 * 
 * <p>JDBC operations use:
 * <ul>
 * <li>{@code java.sql.*} - Standard JDBC API for database connectivity</li>
 * <li>{@code javax.sql.DataSource} - Connection management (via JNDI or direct)</li>
 * <li>{@code RowMapper} - for mapping SQL result sets to entities</li>
 * <li>Parameter binding and prepared statements for SQL injection prevention</li>
 * </ul>
 * 
 * @deprecated Consider migrating to Jakarta Persistence (JPA) for new development
 * @see com.demo.repository.jpa for JPA-based persistence implementations
 */
package com.demo.repository.jdbc;

