# Legacy dependency analysis (scripted, M1)

- Classes: 84; intra-project reference edges: 191
- Edges from explicit imports AND same-package simple-name references (token scan; over-approximates on name collisions, which only tightens coupling groups).

## God nodes (highest fan-in — pin behavior with characterization tests BEFORE converting)

| class | fan-in | fan-out |
|---|---|---|
| org.springframework.samples.petclinic.model.Visit | 18 | 2 |
| org.springframework.samples.petclinic.model.PetType | 18 | 1 |
| org.springframework.samples.petclinic.model.Pet | 17 | 4 |
| org.springframework.samples.petclinic.model.Specialty | 13 | 2 |
| org.springframework.samples.petclinic.model.Owner | 11 | 2 |

## Conversion order (dependencies first — the tree must compile at every commit)

1. org.springframework.samples.petclinic.model.BaseEntity (src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java) — god-node: characterization tests first
2. org.springframework.samples.petclinic.rest.BindingErrorsResponse (src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java) — god-node: characterization tests first
3. MavenWrapperDownloader (.mvn/wrapper/MavenWrapperDownloader.java)
4. org.springframework.samples.petclinic.PetClinicApplication (src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java)
5. org.springframework.samples.petclinic.model.package-info (src/main/java/org/springframework/samples/petclinic/model/package-info.java)
6. org.springframework.samples.petclinic.repository.jdbc.package-info (src/main/java/org/springframework/samples/petclinic/repository/jdbc/package-info.java)
7. org.springframework.samples.petclinic.repository.jpa.package-info (src/main/java/org/springframework/samples/petclinic/repository/jpa/package-info.java)
8. org.springframework.samples.petclinic.rest.ExceptionControllerAdvice (src/main/java/org/springframework/samples/petclinic/rest/ExceptionControllerAdvice.java)
9. org.springframework.samples.petclinic.rest.RootRestController (src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java)
10. org.springframework.samples.petclinic.rest.package-info (src/main/java/org/springframework/samples/petclinic/rest/package-info.java)
11. org.springframework.samples.petclinic.security.BasicAuthenticationConfig (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java)
12. org.springframework.samples.petclinic.security.DisableSecurityConfig (src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java)
13. org.springframework.samples.petclinic.security.Roles (src/main/java/org/springframework/samples/petclinic/security/Roles.java)
14. org.springframework.samples.petclinic.util.ApplicationSwaggerConfig (src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java)
15. org.springframework.samples.petclinic.util.CallMonitoringAspect (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java)
16. org.springframework.samples.petclinic.model.NamedEntity (src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java) — god-node: characterization tests first
17. org.springframework.samples.petclinic.util.EntityUtils (src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java) — god-node: characterization tests first
18. org.springframework.samples.petclinic.model.Person (src/main/java/org/springframework/samples/petclinic/model/Person.java)
19. org.springframework.samples.petclinic.model.PetType (src/main/java/org/springframework/samples/petclinic/model/PetType.java) — god-node: characterization tests first
20. org.springframework.samples.petclinic.repository.PetTypeRepository (src/main/java/org/springframework/samples/petclinic/repository/PetTypeRepository.java) — god-node: characterization tests first
21. org.springframework.samples.petclinic.repository.springdatajpa.PetTypeRepositoryOverride (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/PetTypeRepositoryOverride.java)
22. org.springframework.samples.petclinic.mapper.PetTypeMapper (src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java)
23. org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetTypeRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetTypeRepository.java)

## Circular group (convert together in ONE task)

- org.springframework.samples.petclinic.mapper.OwnerMapper (src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java)
- org.springframework.samples.petclinic.mapper.PetMapper (src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java)
- org.springframework.samples.petclinic.mapper.SpecialtyMapper (src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java)
- org.springframework.samples.petclinic.mapper.UserMapper (src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java)
- org.springframework.samples.petclinic.mapper.VetMapper (src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java)
- org.springframework.samples.petclinic.mapper.VisitMapper (src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java)
- org.springframework.samples.petclinic.model.Owner (src/main/java/org/springframework/samples/petclinic/model/Owner.java)
- org.springframework.samples.petclinic.model.Pet (src/main/java/org/springframework/samples/petclinic/model/Pet.java)
- org.springframework.samples.petclinic.model.Role (src/main/java/org/springframework/samples/petclinic/model/Role.java)
- org.springframework.samples.petclinic.model.Specialty (src/main/java/org/springframework/samples/petclinic/model/Specialty.java)
- org.springframework.samples.petclinic.model.User (src/main/java/org/springframework/samples/petclinic/model/User.java)
- org.springframework.samples.petclinic.model.Vet (src/main/java/org/springframework/samples/petclinic/model/Vet.java)
- org.springframework.samples.petclinic.model.Visit (src/main/java/org/springframework/samples/petclinic/model/Visit.java)
- org.springframework.samples.petclinic.repository.OwnerRepository (src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java)
- org.springframework.samples.petclinic.repository.PetRepository (src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java)
- org.springframework.samples.petclinic.repository.SpecialtyRepository (src/main/java/org/springframework/samples/petclinic/repository/SpecialtyRepository.java)
- org.springframework.samples.petclinic.repository.UserRepository (src/main/java/org/springframework/samples/petclinic/repository/UserRepository.java)
- org.springframework.samples.petclinic.repository.VetRepository (src/main/java/org/springframework/samples/petclinic/repository/VetRepository.java)
- org.springframework.samples.petclinic.repository.VisitRepository (src/main/java/org/springframework/samples/petclinic/repository/VisitRepository.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcOwnerRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcPet (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPet.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcPetRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcPetRowMapper (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRowMapper.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcPetTypeRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetTypeRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcPetVisitExtractor (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetVisitExtractor.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcSpecialtyRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcSpecialtyRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcUserRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcUserRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcVetRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVetRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVisitRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRowMapper (src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVisitRowMapper.java)
- org.springframework.samples.petclinic.repository.jpa.JpaOwnerRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaPetRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaPetTypeRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetTypeRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaSpecialtyRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaSpecialtyRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaUserRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaUserRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaVetRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVetRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.jpa.JpaVisitRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVisitRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.springdatajpa.PetRepositoryOverride (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/PetRepositoryOverride.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpecialtyRepositoryOverride (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpecialtyRepositoryOverride.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataOwnerRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataOwnerRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetTypeRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetTypeRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataSpecialtyRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataSpecialtyRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataSpecialtyRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataSpecialtyRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataUserRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataUserRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVetRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVetRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVisitRepository (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVisitRepository.java)
- org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVisitRepositoryImpl (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVisitRepositoryImpl.java)
- org.springframework.samples.petclinic.repository.springdatajpa.VisitRepositoryOverride (src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/VisitRepositoryOverride.java)
- org.springframework.samples.petclinic.rest.OwnerRestController (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java)
- org.springframework.samples.petclinic.rest.PetRestController (src/main/java/org/springframework/samples/petclinic/rest/PetRestController.java)
- org.springframework.samples.petclinic.rest.PetTypeRestController (src/main/java/org/springframework/samples/petclinic/rest/PetTypeRestController.java)
- org.springframework.samples.petclinic.rest.SpecialtyRestController (src/main/java/org/springframework/samples/petclinic/rest/SpecialtyRestController.java)
- org.springframework.samples.petclinic.rest.UserRestController (src/main/java/org/springframework/samples/petclinic/rest/UserRestController.java)
- org.springframework.samples.petclinic.rest.VetRestController (src/main/java/org/springframework/samples/petclinic/rest/VetRestController.java)
- org.springframework.samples.petclinic.rest.VisitRestController (src/main/java/org/springframework/samples/petclinic/rest/VisitRestController.java)
- org.springframework.samples.petclinic.service.ClinicService (src/main/java/org/springframework/samples/petclinic/service/ClinicService.java)
- org.springframework.samples.petclinic.service.ClinicServiceImpl (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java)
- org.springframework.samples.petclinic.service.UserService (src/main/java/org/springframework/samples/petclinic/service/UserService.java)
- org.springframework.samples.petclinic.service.UserServiceImpl (src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java)
