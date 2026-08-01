-- O-SEEDIMPORT: explicit column names (Hibernate pets column order is
-- id,name,birth_date,owner_id,type_id — positional VALUES from legacy
-- postgresql/populateDB.sql bind wrong and abort the whole script).
INSERT INTO vets (id, first_name, last_name) VALUES (1, 'James', 'Carter') ON CONFLICT DO NOTHING;
INSERT INTO vets (id, first_name, last_name) VALUES (2, 'Helen', 'Leary') ON CONFLICT DO NOTHING;
INSERT INTO vets (id, first_name, last_name) VALUES (3, 'Linda', 'Douglas') ON CONFLICT DO NOTHING;
INSERT INTO vets (id, first_name, last_name) VALUES (4, 'Rafael', 'Ortega') ON CONFLICT DO NOTHING;
INSERT INTO vets (id, first_name, last_name) VALUES (5, 'Henry', 'Stevens') ON CONFLICT DO NOTHING;
INSERT INTO vets (id, first_name, last_name) VALUES (6, 'Sharon', 'Jenkins') ON CONFLICT DO NOTHING;

INSERT INTO specialties (id, name) VALUES (1, 'radiology') ON CONFLICT DO NOTHING;
INSERT INTO specialties (id, name) VALUES (2, 'surgery') ON CONFLICT DO NOTHING;
INSERT INTO specialties (id, name) VALUES (3, 'dentistry') ON CONFLICT DO NOTHING;

INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (3, 2) ON CONFLICT DO NOTHING;
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (3, 3) ON CONFLICT DO NOTHING;
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (4, 2) ON CONFLICT DO NOTHING;
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (5, 1) ON CONFLICT DO NOTHING;

INSERT INTO types (id, name) VALUES (1, 'cat') ON CONFLICT DO NOTHING;
INSERT INTO types (id, name) VALUES (2, 'dog') ON CONFLICT DO NOTHING;
INSERT INTO types (id, name) VALUES (3, 'lizard') ON CONFLICT DO NOTHING;
INSERT INTO types (id, name) VALUES (4, 'snake') ON CONFLICT DO NOTHING;
INSERT INTO types (id, name) VALUES (5, 'bird') ON CONFLICT DO NOTHING;
INSERT INTO types (id, name) VALUES (6, 'hamster') ON CONFLICT DO NOTHING;

INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435') ON CONFLICT DO NOTHING;
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487') ON CONFLICT DO NOTHING;

INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (1, 'Leo', '2000-09-07', 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (2, 'Basil', '2002-08-06', 6, 2) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (3, 'Rosy', '2001-04-17', 2, 3) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (4, 'Jewel', '2000-03-07', 2, 3) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (5, 'Iggy', '2000-11-30', 3, 4) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (6, 'George', '2000-01-20', 4, 5) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (7, 'Samantha', '1995-09-04', 1, 6) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (8, 'Max', '1995-09-04', 1, 6) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (9, 'Lucky', '1999-08-06', 5, 7) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (10, 'Mulligan', '1997-02-24', 2, 8) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (11, 'Freddy', '2000-03-09', 5, 9) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (12, 'Lucky', '2000-06-24', 2, 10) ON CONFLICT DO NOTHING;
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (13, 'Sly', '2002-06-08', 1, 10) ON CONFLICT DO NOTHING;

INSERT INTO visits (id, pet_id, visit_date, description) VALUES (1, 7, '2010-03-04', 'rabies shot') ON CONFLICT DO NOTHING;
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (2, 8, '2011-03-04', 'rabies shot') ON CONFLICT DO NOTHING;
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (3, 8, '2009-06-04', 'neutered') ON CONFLICT DO NOTHING;
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (4, 7, '2008-09-04', 'spayed') ON CONFLICT DO NOTHING;

INSERT INTO users (username, password, enabled) VALUES ('admin', '{noop}admin', true) ON CONFLICT DO NOTHING;
INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_OWNER_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_VET_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_ADMIN') ON CONFLICT DO NOTHING;
