/**
 * The classes in this package represent PetClinic's business layer.
 * 
 * <p>This package contains JPA entity classes using Jakarta Persistence API 3.0
 * annotations for object-relational mapping. All entities extend {@code BaseEntity}
 * which provides common identifier and lifecycle management functionality.
 * 
 * <p>Jakarta Persistence annotations used include:
 * <ul>
 * <li>{@code @Entity} - marks classes as JPA entities</li>
 * <li>{@code @Table} - specifies database table mappings</li>
 * <li>{@code @Id} and {@code @GeneratedValue} - define primary key generation</li>
 * <li>{@code @Column} - maps entity properties to database columns</li>
 * <li>{@code @ManyToOne}, {@code @OneToMany} - define entity relationships</li>
 * </ul>
 * 
 * @since Jakarta EE 9 / Jakarta Persistence 3.0
 */
package com.demo.model;

