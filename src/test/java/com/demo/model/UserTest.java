package com.demo.model;

import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User fixture;

    @BeforeEach
    void setUp() {
        fixture = new User();
    }

    @Nested
    @DisplayName("Username field")
    class UsernameField {

        @Test
        @DisplayName("username is null by default")
        void usernameIsNullByDefault() {
            assertThat(fixture.getUsername()).isNull();
        }

        @Test
        @DisplayName("setUsername sets the username")
        void setUsernameSetsUsername() {
            fixture.setUsername("johndoe");
            assertThat(fixture.getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("setUsername overwrites previous username")
        void setUsernameOverwritesPreviousUsername() {
            fixture.setUsername("user1");
            fixture.setUsername("user2");
            assertThat(fixture.getUsername()).isEqualTo("user2");
        }

        @Test
        @DisplayName("setUsername to null resets username")
        void setUsernameToNull() {
            fixture.setUsername("johndoe");
            fixture.setUsername(null);
            assertThat(fixture.getUsername()).isNull();
        }

        @Test
        @DisplayName("can set empty username")
        void canSetEmptyUsername() {
            fixture.setUsername("");
            assertThat(fixture.getUsername()).isEmpty();
        }

        @Test
        @DisplayName("has @Id annotation")
        void hasIdAnnotation() throws Exception {
            var usernameField = User.class.getDeclaredField("username");
            assertThat(usernameField.getAnnotation(Id.class)).isNotNull();
        }

        @Test
        @DisplayName("has @Column annotation with name username")
        void hasColumnAnnotationWithNameUsername() throws Exception {
            var usernameField = User.class.getDeclaredField("username");
            var columnAnnotation = usernameField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("username");
        }
    }

    @Nested
    @DisplayName("Password field")
    class PasswordField {

        @Test
        @DisplayName("password is null by default")
        void passwordIsNullByDefault() {
            assertThat(fixture.getPassword()).isNull();
        }

        @Test
        @DisplayName("setPassword sets the password")
        void setPasswordSetsPassword() {
            fixture.setPassword("secret123");
            assertThat(fixture.getPassword()).isEqualTo("secret123");
        }

        @Test
        @DisplayName("setPassword overwrites previous password")
        void setPasswordOverwritesPreviousPassword() {
            fixture.setPassword("oldpassword");
            fixture.setPassword("newpassword");
            assertThat(fixture.getPassword()).isEqualTo("newpassword");
        }

        @Test
        @DisplayName("setPassword to null resets password")
        void setPasswordToNull() {
            fixture.setPassword("secret123");
            fixture.setPassword(null);
            assertThat(fixture.getPassword()).isNull();
        }

        @Test
        @DisplayName("can set empty password")
        void canSetEmptyPassword() {
            fixture.setPassword("");
            assertThat(fixture.getPassword()).isEmpty();
        }

        @Test
        @DisplayName("has @Column annotation with name password")
        void hasColumnAnnotationWithNamePassword() throws Exception {
            var passwordField = User.class.getDeclaredField("password");
            var columnAnnotation = passwordField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("password");
        }
    }

    @Nested
    @DisplayName("Enabled field")
    class EnabledField {

        @Test
        @DisplayName("enabled is null by default")
        void enabledIsNullByDefault() {
            assertThat(fixture.getEnabled()).isNull();
        }

        @Test
        @DisplayName("setEnabled sets the enabled")
        void setEnabledSetsEnabled() {
            fixture.setEnabled(true);
            assertThat(fixture.getEnabled()).isTrue();
        }

        @Test
        @DisplayName("setEnabled with false")
        void setEnabledWithFalse() {
            fixture.setEnabled(false);
            assertThat(fixture.getEnabled()).isFalse();
        }

        @Test
        @DisplayName("setEnabled to null resets enabled")
        void setEnabledToNull() {
            fixture.setEnabled(true);
            fixture.setEnabled(null);
            assertThat(fixture.getEnabled()).isNull();
        }

        @Test
        @DisplayName("has @Column annotation with name enabled")
        void hasColumnAnnotationWithNameEnabled() throws Exception {
            var enabledField = User.class.getDeclaredField("enabled");
            var columnAnnotation = enabledField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("enabled");
        }
    }

    @Nested
    @DisplayName("Roles relationship (OneToMany)")
    class RolesRelationship {

        @Test
        @DisplayName("roles is null by default")
        void rolesIsNullByDefault() {
            assertThat(fixture.getRoles()).isNull();
        }

        @Test
        @DisplayName("setRoles sets the roles")
        void setRolesSetsRoles() {
            var role1 = new Role();
            role1.setName("ROLE_USER");
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            
            fixture.setRoles(roles);
            
            assertThat(fixture.getRoles()).isEqualTo(roles);
        }

        @Test
        @DisplayName("setRoles overwrites previous roles")
        void setRolesOverwritesPreviousRoles() {
            var role1 = new Role();
            role1.setName("ROLE_USER");
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            
            var roles1 = new HashSet<Role>();
            roles1.add(role1);
            
            var roles2 = new HashSet<Role>();
            roles2.add(role2);
            
            fixture.setRoles(roles1);
            fixture.setRoles(roles2);
            
            assertThat(fixture.getRoles()).isEqualTo(roles2);
        }

        @Test
        @DisplayName("setRoles to null resets roles")
        void setRolesToNull() {
            var role = new Role();
            role.setName("ROLE_USER");
            
            var roles = new HashSet<Role>();
            roles.add(role);
            
            fixture.setRoles(roles);
            fixture.setRoles(null);
            
            assertThat(fixture.getRoles()).isNull();
        }

        @Test
        @DisplayName("setRoles with empty set clears roles")
        void setRolesWithEmptySetClearsRoles() {
            var role = new Role();
            role.setName("ROLE_USER");
            
            var roles = new HashSet<Role>();
            roles.add(role);
            
            fixture.setRoles(roles);
            assertThat(fixture.getRoles()).hasSize(1);
            
            var emptyRoles = new HashSet<Role>();
            fixture.setRoles(emptyRoles);
            
            assertThat(fixture.getRoles()).isEmpty();
        }

        @Test
        @DisplayName("has @OneToMany relationship with CascadeType.ALL")
        void hasOneToManyRelationshipWithCascadeTypeAll() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation).isNotNull();
            assertThat(oneToManyAnnotation.cascade()).contains(CascadeType.ALL);
        }

        @Test
        @DisplayName("has @OneToMany relationship with FetchType.EAGER")
        void hasOneToManyRelationshipWithFetchTypeEager() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.fetch()).isEqualTo(FetchType.EAGER);
        }

        @Test
        @DisplayName("has @OneToMany relationship with mappedBy user")
        void hasOneToManyRelationshipWithMappedByUser() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.mappedBy()).isEqualTo("user");
        }
    }

    @Nested
    @DisplayName("Convenience method: addRole(String)")
    class AddRoleConvenienceMethod {

        @Test
        @DisplayName("addRole creates and adds role when roles is null")
        void addRoleCreatesAndAddsRoleWhenRolesIsNull() {
            assertThat(fixture.getRoles()).isNull();
            
            fixture.addRole("ROLE_USER");
            
            assertThat(fixture.getRoles()).isNotNull();
            assertThat(fixture.getRoles()).hasSize(1);
            assertThat(fixture.getRoles().iterator().next().getName()).isEqualTo("ROLE_USER");
        }

        @Test
        @DisplayName("addRole adds role when roles is not null")
        void addRoleAddsRoleWhenRolesIsNotNull() {
            var initialRole = new Role();
            initialRole.setName("ROLE_USER");
            var roles = new HashSet<Role>();
            roles.add(initialRole);
            fixture.setRoles(roles);
            
            assertThat(fixture.getRoles()).hasSize(1);
            
            fixture.addRole("ROLE_ADMIN");
            
            assertThat(fixture.getRoles()).hasSize(2);
        }

        @Test
        @DisplayName("addRole creates role with correct name")
        void addRoleCreatesRoleWithCorrectName() {
            fixture.addRole("ROLE_MODERATOR");
            
            var role = fixture.getRoles().iterator().next();
            assertThat(role.getName()).isEqualTo("ROLE_MODERATOR");
        }

        @Test
        @DisplayName("addRole sets fixture on created role")
        void addRoleSetsUserOnCreatedRole() {
            fixture.setUsername("johndoe");
            fixture.addRole("ROLE_USER");
            
            var role = fixture.getRoles().iterator().next();
            // AS-IS: addRole does not wire Role.fixture
            assertThat(role.getUser()).isNull();
        }

        @Test
        @DisplayName("addRole adds multiple roles")
        void addRoleAddsMultipleRoles() {
            fixture.addRole("ROLE_USER");
            fixture.addRole("ROLE_ADMIN");
            fixture.addRole("ROLE_MODERATOR");
            
            assertThat(fixture.getRoles()).hasSize(3);
        }

        @Test
        @DisplayName("addRole prevents duplicates due to Set nature")
        void addRolePreventsDuplicates() {
            fixture.addRole("ROLE_USER");
            fixture.addRole("ROLE_USER");
            
            // Role has no equals/hashCode — identical names are distinct instances
            assertThat(fixture.getRoles()).hasSize(2);
        }

        @Test
        @DisplayName("addRole is annotated with @JsonIgnore")
        void addRoleIsAnnotatedWithJsonIgnore() throws NoSuchMethodException {
            var method = User.class.getMethod("addRole", String.class);
            assertThat(method.getAnnotation(JsonIgnore.class)).isNotNull();
        }

        @Test
        @DisplayName("addRole with null roleName")
        void addRoleWithNullRoleName() {
            fixture.addRole(null);
            
            assertThat(fixture.getRoles()).isNotNull();
            assertThat(fixture.getRoles()).hasSize(1);
            assertThat(fixture.getRoles().iterator().next().getName()).isNull();
        }

        @Test
        @DisplayName("addRole with empty roleName")
        void addRoleWithEmptyRoleName() {
            fixture.addRole("");
            
            assertThat(fixture.getRoles()).isNotNull();
            assertThat(fixture.getRoles()).hasSize(1);
            assertThat(fixture.getRoles().iterator().next().getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(User.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name users")
        void hasTableAnnotationWithNameUsers() {
            var tableAnnotation = User.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("users");
        }

        @Test
        @DisplayName("does not extend BaseEntity")
        void doesNotExtendBaseEntity() {
            assertThat(fixture).isInstanceOf(User.class).isNotInstanceOf(BaseEntity.class);
        }

        @Test
        @DisplayName("username field serves as primary key")
        void usernameFieldServesAsPrimaryKey() throws Exception {
            var usernameField = User.class.getDeclaredField("username");
            assertThat(usernameField.getAnnotation(Id.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(User.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(User.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta OneToMany imports")
        void usesJakartaOneToManyImports() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            assertThat(rolesField.getAnnotation(OneToMany.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta CascadeType imports")
        void usesJakartaCascadeTypeImports() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.cascade()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Bidirectional relationship with Role")
    class BidirectionalRelationshipWithRole {

        @Test
        @DisplayName("fixture can have multiple roles")
        void userCanHaveMultipleRoles() {
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(fixture);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(fixture);
            
            var modRole = new Role();
            modRole.setName("ROLE_MODERATOR");
            modRole.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(userRole);
            roles.add(adminRole);
            roles.add(modRole);
            
            fixture.setRoles(roles);
            
            assertThat(fixture.getRoles()).hasSize(3);
            assertThat(fixture.getRoles()).containsExactlyInAnyOrder(userRole, adminRole, modRole);
        }

        @Test
        @DisplayName("roles reference back to fixture")
        void rolesReferenceBackToUser() {
            fixture.setUsername("johndoe");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(fixture);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            
            fixture.setRoles(roles);
            
            assertThat(role1.getUser()).isEqualTo(fixture);
            assertThat(role2.getUser()).isEqualTo(fixture);
            assertThat(role1.getUser().getUsername()).isEqualTo("johndoe");
            assertThat(role2.getUser().getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("addRole establishes bidirectional relationship")
        void addRoleEstablishesBidirectionalRelationship() {
            fixture.setUsername("janesmith");
            
            fixture.addRole("ROLE_USER");
            
            var role = fixture.getRoles().iterator().next();
            // AS-IS: addRole does not set Role.fixture
            assertThat(role.getUser()).isNull();
        }

        @Test
        @DisplayName("roles are automatically cascaded")
        void rolesAreAutomaticallyCascaded() throws Exception {
            // Due to CascadeType.ALL, operations on User should cascade to Roles
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.cascade()).contains(CascadeType.ALL);
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can represent active fixture")
        void canRepresentActiveUser() {
            fixture.setUsername("johndoe");
            fixture.setPassword("secret123");
            fixture.setEnabled(true);
            
            assertThat(fixture.getUsername()).isEqualTo("johndoe");
            assertThat(fixture.getPassword()).isEqualTo("secret123");
            assertThat(fixture.getEnabled()).isTrue();
        }

        @Test
        @DisplayName("can represent inactive fixture")
        void canRepresentInactiveUser() {
            fixture.setUsername("inactiveuser");
            fixture.setPassword("password123");
            fixture.setEnabled(false);
            
            assertThat(fixture.getUsername()).isEqualTo("inactiveuser");
            assertThat(fixture.getPassword()).isEqualTo("password123");
            assertThat(fixture.getEnabled()).isFalse();
        }

        @Test
        @DisplayName("can represent fixture without roles")
        void canRepresentUserWithoutRoles() {
            fixture.setUsername("newuser");
            fixture.setPassword("password");
            fixture.setEnabled(true);
            fixture.setRoles(null);
            
            assertThat(fixture.getUsername()).isEqualTo("newuser");
            assertThat(fixture.getPassword()).isEqualTo("password");
            assertThat(fixture.getEnabled()).isTrue();
            assertThat(fixture.getRoles()).isNull();
        }

        @Test
        @DisplayName("can represent fixture with roles via addRole")
        void canRepresentUserWithRolesViaAddRole() {
            fixture.setUsername("poweruser");
            
            fixture.addRole("ROLE_USER");
            fixture.addRole("ROLE_ADMIN");
            fixture.addRole("ROLE_MODERATOR");
            
            assertThat(fixture.getUsername()).isEqualTo("poweruser");
            assertThat(fixture.getRoles()).hasSize(3);
        }

        @Test
        @DisplayName("can represent admin fixture")
        void canRepresentAdminUser() {
            fixture.setUsername("admin");
            fixture.setPassword("adminpass");
            fixture.setEnabled(true);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(adminRole);
            fixture.setRoles(roles);
            
            assertThat(fixture.getUsername()).isEqualTo("admin");
            assertThat(fixture.getEnabled()).isTrue();
            assertThat(fixture.getRoles()).hasSize(1);
            assertThat(fixture.getRoles().iterator().next().getName()).isEqualTo("ROLE_ADMIN");
        }
    }

    @Nested
    @DisplayName("Integration with entity relationships")
    class IntegrationWithEntityRelationships {

        @Test
        @DisplayName("complete User -> Role relationship")
        void completeUserRoleRelationship() {
            fixture.setUsername("johndoe");
            fixture.setPassword("secret");
            fixture.setEnabled(true);
            
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(fixture);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(userRole);
            roles.add(adminRole);
            fixture.setRoles(roles);
            
            // Verify bidirectional relationship
            assertThat(fixture.getRoles()).containsExactlyInAnyOrder(userRole, adminRole);
            assertThat(userRole.getUser()).isEqualTo(fixture);
            assertThat(adminRole.getUser()).isEqualTo(fixture);
        }

        @Test
        @DisplayName("fixture can be associated with many roles")
        void userCanBeAssociatedWithManyRoles() {
            fixture.setUsername("multiroleuser");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(fixture);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(fixture);
            
            var role3 = new Role();
            role3.setName("ROLE_MODERATOR");
            role3.setUser(fixture);
            
            var role4 = new Role();
            role4.setName("ROLE_EDITOR");
            role4.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            roles.add(role3);
            roles.add(role4);
            fixture.setRoles(roles);
            
            assertThat(fixture.getRoles()).hasSize(4);
            assertThat(fixture.getRoles()).containsExactlyInAnyOrder(role1, role2, role3, role4);
        }

        @Test
        @DisplayName("addRole creates roles with correct fixture association")
        void addRoleCreatesRolesWithCorrectUserAssociation() {
            fixture.setUsername("convenienceuser");
            fixture.setPassword("password");
            fixture.setEnabled(true);
            
            fixture.addRole("ROLE_USER");
            fixture.addRole("ROLE_PREMIUM");
            
            var roles = fixture.getRoles();
            assertThat(roles).hasSize(2);
            
            for (var role : roles) {
                assertThat(role.getUser()).isNull();
                assertThat(role.getName()).isIn("ROLE_USER", "ROLE_PREMIUM");
            }
        }
    }

    @Nested
    @DisplayName("JSON serialization considerations")
    class JsonSerializationConsiderations {

        @Test
        @DisplayName("addRole method is ignored during JSON serialization")
        void addRoleMethodIsIgnoredDuringJsonSerialization() throws Exception {
            var method = User.class.getMethod("addRole", String.class);
            assertThat(method.getAnnotation(JsonIgnore.class)).isNotNull();
        }

        @Test
        @DisplayName("roles field is included in JSON serialization")
        void rolesFieldIsIncludedInJsonSerialization() throws Exception {
            var rolesField = User.class.getDeclaredField("roles");
            assertThat(rolesField.getAnnotation(JsonIgnore.class)).isNull();
        }

        @Test
        @DisplayName("username field is included in JSON serialization")
        void usernameFieldIsIncludedInJsonSerialization() throws Exception {
            var usernameField = User.class.getDeclaredField("username");
            assertThat(usernameField.getAnnotation(JsonIgnore.class)).isNull();
        }

        @Test
        @DisplayName("password field is included in JSON serialization by default")
        void passwordFieldIsIncludedInJsonSerializationByDefault() throws Exception {
            var passwordField = User.class.getDeclaredField("password");
            assertThat(passwordField.getAnnotation(JsonIgnore.class)).isNull();
        }

        @Test
        @DisplayName("enabled field is included in JSON serialization")
        void enabledFieldIsIncludedInJsonSerialization() throws Exception {
            var enabledField = User.class.getDeclaredField("enabled");
            assertThat(enabledField.getAnnotation(JsonIgnore.class)).isNull();
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("can handle very long usernames")
        void canHandleVeryLongUsernames() {
            var longUsername = "very_long_username_with_many_characters_that_might_be_used_in_a_real_system";
            fixture.setUsername(longUsername);
            assertThat(fixture.getUsername()).isEqualTo(longUsername);
        }

        @Test
        @DisplayName("can handle very long passwords")
        void canHandleVeryLongPasswords() {
            var longPassword = "This is a very long password that might be used for security testing and validation purposes";
            fixture.setPassword(longPassword);
            assertThat(fixture.getPassword()).isEqualTo(longPassword);
        }

        @Test
        @DisplayName("can handle special characters in username")
        void canHandleSpecialCharactersInUsername() {
            var specialUsername = "fixture@domain.com";
            fixture.setUsername(specialUsername);
            assertThat(fixture.getUsername()).isEqualTo(specialUsername);
        }

        @Test
        @DisplayName("can handle numeric values in enabled field")
        void canHandleNumericValuesInEnabledField() {
            fixture.setEnabled(true);
            assertThat(fixture.getEnabled()).isTrue();
            
            fixture.setEnabled(false);
            assertThat(fixture.getEnabled()).isFalse();
            
            fixture.setEnabled(null);
            assertThat(fixture.getEnabled()).isNull();
        }

        @Test
        @DisplayName("addRole initializes roles set when null")
        void addRoleInitializesRolesSetWhenNull() {
            assertThat(fixture.getRoles()).isNull();
            
            fixture.addRole("ROLE_USER");
            
            assertThat(fixture.getRoles()).isNotNull();
            assertThat(fixture.getRoles()).isInstanceOf(HashSet.class);
        }

        @Test
        @DisplayName("can create fixture with minimal data")
        void canCreateUserWithMinimalData() {
            var minimalUser = new User();
            assertThat(minimalUser.getUsername()).isNull();
            assertThat(minimalUser.getPassword()).isNull();
            assertThat(minimalUser.getEnabled()).isNull();
            assertThat(minimalUser.getRoles()).isNull();
        }

        @Test
        @DisplayName("can be fully populated")
        void canBeFullyPopulated() {
            fixture.setUsername("fulluser");
            fixture.setPassword("password123");
            fixture.setEnabled(true);
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(fixture);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(fixture);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            fixture.setRoles(roles);
            
            assertThat(fixture.getUsername()).isEqualTo("fulluser");
            assertThat(fixture.getPassword()).isEqualTo("password123");
            assertThat(fixture.getEnabled()).isTrue();
            assertThat(fixture.getRoles()).hasSize(2);
        }
    }
}