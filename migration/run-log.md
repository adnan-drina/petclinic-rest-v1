# Harness run log

Appended by the Hermes orchestrator after every task (see
`.hermes/skills/migration-harness/`). One line per task.

||| Task | Class | Attempts | Result | Files |
||---|---|---|---|---|
||T-001 | rewrite | 1 | SUCCESS | Created missing src/test/java/com/demo/util/.gitkeep file; package structure now complete
||T-003 | rewrite | 1 | SUCCESS | Harvested and updated 4 package-info.java files with Jakarta documentation (model, repository/jdbc, repository/jpa, rest)

## M5 Evaluation - Final Status

### Findings Delta Analysis
**METRIC src_main_java=6 src_test_java=3**
**METRIC residual_incidents src/main=3 src/test=0 pom=6 props=0 other=0**

**Honest resolve percentage: 42.9% (12 resolved out of 28 actionable findings)**

### Detailed Findings Classification:

#### RESOLVED (12 findings - landed evidence + rule absent after):
1. **hibernate-00005** - Resolved: Implicit name determination for sequences and tables
2. **javax-to-jakarta-dependencies-00001** - Resolved: 'javax' groupId replaced by 'jakarta' 
3. **javax-to-jakarta-dependencies-00003** - Resolved: javax.xml.bind jaxb-api artifact replacement
4. **javax-to-jakarta-import-00001** - Resolved: Package 'javax' replaced by 'jakarta'
5. **spring-components-00001** - Resolved: Spring Boot version compatibility with Jakarta EE 9+
6. **spring-components-00002** - Resolved: Spring version compatibility fix
7. **springboot-actuator-to-quarkus-0100** - Resolved: Spring Boot Actuator replacement with Quarkus
8. **springboot-cache-to-quarkus-00000** - Resolved: SpringBoot cache artifact replacement
9. **springboot-devservices-to-quarkus-00000** - Resolved: Quarkus Dev Services adoption
10. **springboot-jpa-to-quarkus-00000** - Resolved: SpringBoot Data JPA artifact replacement
11. **springboot-properties-to-quarkus-00003** - Resolved: Spring log level property replacement
12. **springboot-security-to-quarkus-00000** - Resolved: SpringBoot Security artifact replacement

#### ABSENT-NOT-LANDED (11 findings - rule gone but nothing in src/ - 0 story credit):
1. **localhost-jdbc-00002** - Absent: Local JDBC calls - no src/ evidence
2. **persistence-to-quarkus-00010** - Absent: @PersistenceContext to @Inject - no src/ evidence  
3. **removed-javaee-modules-00020** - Absent: java.annotation module removal - no src/ evidence
4. **springboot-annotations-to-quarkus-00002** - Absent: ComponentScan replacement - no src/ evidence
5. **springboot-di-to-quarkus-00002** - Absent: Spring DI infrastructure replacement - no src/ evidence
6. **springboot-di-to-quarkus-00003** - Absent: Quarkus Spring DI conversion - no src/ evidence
7. **springboot-jmx-to-quarkus-00001** - Absent: Spring JMX annotations - no src/ evidence
8. **springboot-properties-to-quarkus-00001** - Absent: Spring property profiles refactoring - no src/ evidence
9. **springboot-properties-to-quarkus-00002** - Absent: Spring datasource properties replacement - no src/ evidence
10. **springboot-webmvc-to-quarkus-00000** - Absent: Spring MVC not supported by Quarkus - no src/ evidence
11. **transaction-to-quarkus-00003** - Absent: EntityManager @Transactional requirement - no src/ evidence

#### SCAFFOLD-PRESATISFIED (9 findings - destination already satisfied - no story credit):
1. **javaee-pom-to-quarkus-00010** - Presatisfied: Quarkus BOM adoption
2. **javaee-pom-to-quarkus-00020** - Presatisfied: Quarkus Maven plugin adoption
3. **javaee-pom-to-quarkus-00040** - Presatisfied: Maven Surefire plugin adoption
4. **springboot-annotations-to-quarkus-00000** - Presatisfied: Quarkus bootstrap model
5. **springboot-di-to-quarkus-00000** - Presatisfied: Quarkus Spring DI artifact
6. **springboot-parent-pom-to-quarkus-00000** - Presatisfied: Quarkus BOM parent
7. **springboot-plugins-to-quarkus-0000** - Presatisfied: spring-boot-maven-plugin replacement
8. **springboot-properties-to-quarkus-00000** - Presatisfied: Quarkus spring-boot-properties artifact
9. **springboot-web-to-quarkus-00000** - Presatisfied: Quarkus spring-web artifact

#### REMAINING (5 findings - still in after-scan):
1. **javaee-pom-to-quarkus-00030** - Remaining: Maven Compiler plugin adoption needed
2. **javaee-pom-to-quarkus-00050** - Remaining: Maven Failsafe plugin adoption needed  
3. **javaee-pom-to-quarkus-00060** - Remaining: Native build profile addition needed
4. **springboot-metrics-to-quarkus-0100** - Remaining: Micrometer dependency replacement needed
5. **springboot-metrics-to-quarkus-0200** - Remaining: Micrometer code replacement needed

#### NEW IN AFTER (2 findings - not in before):
1. **demo-env-integration-00001** - New after: Environment integration finding
2. **jakarta-jaxrs-to-quarkus-00010** - New after: Jakarta JAXRS to Quarkus finding

### Preflight Status:
**PREFLIGHT RED** - SonarQube quality gate failed (exit code 1)
- sonar_check failed: "quality gate red — violations above; full log /tmp/sensor-sonar.log"
- Maven verify passed successfully (clean build and tests)
- JaCoCo coverage check passed (qjacoco check GREEN)
- Code violations detected by SonarQube quality gate
- Commit message must reflect RED preflight status honestly

### Debt Summary:
No migration debt recorded - all attempted tasks completed successfully within iteration budget.

### Overall Assessment:
Migration achieved 42.9% honest resolution rate (12/28 actionable findings resolved).
Remaining findings are primarily POM configuration items and metrics framework updates.
Preflight RED due to SonarQube quality gate failure requiring additional code quality fixes.
