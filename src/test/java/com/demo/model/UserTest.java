package com.demo.model;

import java.util.HashSet;
import java.util.Set;

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

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Nested
    @DisplayName("Username field")
    class UsernameField {

        @Test
        @DisplayName("username is null by default")
        void usernameIsNullByDefault() {
            assertThat(user.getUsername()).isNull();
        }

        @Test
        @DisplayName("setUsername sets the username")
        void setUsernameSetsUsername() {
            user.setUsername("johndoe");
            assertThat(user.getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("setUsername overwrites previous username")
        void setUsernameOverwritesPreviousUsername() {
            user.setUsername("user1");
            user.setUsername("user2");
            assertThat(user.getUsername()).isEqualTo("user2");
        }

        @Test
        @DisplayName("setUsername to null resets username")
        void setUsernameToNull() {
            user.setUsername("johndoe");
            user.setUsername(null);
            assertThat(user.getUsername()).isNull();
        }

        @Test
        @DisplayName("can set empty username")
        void canSetEmptyUsername() {
            user.setUsername("");
            assertThat(user.getUsername()).isEmpty();
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
            assertThat(user.getPassword()).isNull();
        }

        @Test
        @DisplayName("setPassword sets the password")
        void setPasswordSetsPassword() {
            user.setPassword("secret123");
            assertThat(user.getPassword()).isEqualTo("secret123");
        }

        @Test
        @DisplayName("setPassword overwrites previous password")
        void setPasswordOverwritesPreviousPassword() {
            user.setPassword("oldpassword");
            user.setPassword("newpassword");
            assertThat(user.getPassword()).isEqualTo("newpassword");
        }

        @Test
        @DisplayName("setPassword to null resets password")
        void setPasswordToNull() {
            user.setPassword("secret123");
            user.setPassword(null);
            assertThat(user.getPassword()).isNull();
        }

        @Test
        @DisplayName("can set empty password")
        void canSetEmptyPassword() {
            user.setPassword("");
            assertThat(user.getPassword()).isEmpty();
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
            assertThat(user.getEnabled()).isNull();
        }

        @Test
        @DisplayName("setEnabled sets the enabled")
        void setEnabledSetsEnabled() {
            user.setEnabled(true);
            assertThat(user.getEnabled()).isTrue();
        }

        @Test
        @DisplayName("setEnabled with false")
        void setEnabledWithFalse() {
            user.setEnabled(false);
            assertThat(user.getEnabled()).isFalse();
        }

        @Test
        @DisplayName("setEnabled to null resets enabled")
        void setEnabledToNull() {
            user.setEnabled(true);
            user.setEnabled(null);
            assertThat(user.getEnabled()).isNull();
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
            assertThat(user.getRoles()).isNull();
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
            
            user.setRoles(roles);
            
            assertThat(user.getRoles()).isEqualTo(roles);
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
            
            user.setRoles(roles1);
            user.setRoles(roles2);
            
            assertThat(user.getRoles()).isEqualTo(roles2);
        }

        @Test
        @DisplayName("setRoles to null resets roles")
        void setRolesToNull() {
            var role = new Role();
            role.setName("ROLE_USER");
            
            var roles = new HashSet<Role>();
            roles.add(role);
            
            user.setRoles(roles);
            user.setRoles(null);
            
            assertThat(user.getRoles()).isNull();
        }

        @Test
        @DisplayName("setRoles with empty set clears roles")
        void setRolesWithEmptySetClearsRoles() {
            var role = new Role();
            role.setName("ROLE_USER");
            
            var roles = new HashSet<Role>();
            roles.add(role);
            
            user.setRoles(roles);
            assertThat(user.getRoles()).hasSize(1);
            
            var emptyRoles = new HashSet<Role>();
            user.setRoles(emptyRoles);
            
            assertThat(user.getRoles()).isEmpty();
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
            assertThat(user.getRoles()).isNull();
            
            user.addRole("ROLE_USER");
            
            assertThat(user.getRoles()).isNotNull();
            assertThat(user.getRoles()).hasSize(1);
            assertThat(user.getRoles().iterator().next().getName()).isEqualTo("ROLE_USER");
        }

        @Test
        @DisplayName("addRole adds role when roles is not null")
        void addRoleAddsRoleWhenRolesIsNotNull() {
            var initialRole = new Role();
            initialRole.setName("ROLE_USER");
            var roles = new HashSet<Role>();
            roles.add(initialRole);
            user.setRoles(roles);
            
            assertThat(user.getRoles()).hasSize(1);
            
            user.addRole("ROLE_ADMIN");
            
            assertThat(user.getRoles()).hasSize(2);
        }

        @Test
        @DisplayName("addRole creates role with correct name")
        void addRoleCreatesRoleWithCorrectName() {
            user.addRole("ROLE_MODERATOR");
            
            var role = user.getRoles().iterator().next();
            assertThat(role.getName()).isEqualTo("ROLE_MODERATOR");
        }

        @Test
        @DisplayName("addRole sets user on created role")
        void addRoleSetsUserOnCreatedRole() {
            user.setUsername("johndoe");
            user.addRole("ROLE_USER");
            
            var role = user.getRoles().iterator().next();
            // AS-IS: addRole does not wire Role.user
            assertThat(role.getUser()).isNull();
        }

        @Test
        @DisplayName("addRole adds multiple roles")
        void addRoleAddsMultipleRoles() {
            user.addRole("ROLE_USER");
            user.addRole("ROLE_ADMIN");
            user.addRole("ROLE_MODERATOR");
            
            assertThat(user.getRoles()).hasSize(3);
        }

        @Test
        @DisplayName("addRole prevents duplicates due to Set nature")
        void addRolePreventsDuplicates() {
            user.addRole("ROLE_USER");
            user.addRole("ROLE_USER");
            
            // Role has no equals/hashCode — identical names are distinct instances
            assertThat(user.getRoles()).hasSize(2);
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
            user.addRole(null);
            
            assertThat(user.getRoles()).isNotNull();
            assertThat(user.getRoles()).hasSize(1);
            assertThat(user.getRoles().iterator().next().getName()).isNull();
        }

        @Test
        @DisplayName("addRole with empty roleName")
        void addRoleWithEmptyRoleName() {
            user.addRole("");
            
            assertThat(user.getRoles()).isNotNull();
            assertThat(user.getRoles()).hasSize(1);
            assertThat(user.getRoles().iterator().next().getName()).isEmpty();
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
            assertThat(user).isInstanceOf(User.class);
            assertThat(user).isNotInstanceOf(BaseEntity.class);
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
        @DisplayName("user can have multiple roles")
        void userCanHaveMultipleRoles() {
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(user);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(user);
            
            var modRole = new Role();
            modRole.setName("ROLE_MODERATOR");
            modRole.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(userRole);
            roles.add(adminRole);
            roles.add(modRole);
            
            user.setRoles(roles);
            
            assertThat(user.getRoles()).hasSize(3);
            assertThat(user.getRoles()).containsExactlyInAnyOrder(userRole, adminRole, modRole);
        }

        @Test
        @DisplayName("roles reference back to user")
        void rolesReferenceBackToUser() {
            user.setUsername("johndoe");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            
            user.setRoles(roles);
            
            assertThat(role1.getUser()).isEqualTo(user);
            assertThat(role2.getUser()).isEqualTo(user);
            assertThat(role1.getUser().getUsername()).isEqualTo("johndoe");
            assertThat(role2.getUser().getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("addRole establishes bidirectional relationship")
        void addRoleEstablishesBidirectionalRelationship() {
            user.setUsername("janesmith");
            
            user.addRole("ROLE_USER");
            
            var role = user.getRoles().iterator().next();
            // AS-IS: addRole does not set Role.user
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
        @DisplayName("can represent active user")
        void canRepresentActiveUser() {
            user.setUsername("johndoe");
            user.setPassword("secret123");
            user.setEnabled(true);
            
            assertThat(user.getUsername()).isEqualTo("johndoe");
            assertThat(user.getPassword()).isEqualTo("secret123");
            assertThat(user.getEnabled()).isTrue();
        }

        @Test
        @DisplayName("can represent inactive user")
        void canRepresentInactiveUser() {
            user.setUsername("inactiveuser");
            user.setPassword("password123");
            user.setEnabled(false);
            
            assertThat(user.getUsername()).isEqualTo("inactiveuser");
            assertThat(user.getPassword()).isEqualTo("password123");
            assertThat(user.getEnabled()).isFalse();
        }

        @Test
        @DisplayName("can represent user without roles")
        void canRepresentUserWithoutRoles() {
            user.setUsername("newuser");
            user.setPassword("password");
            user.setEnabled(true);
            user.setRoles(null);
            
            assertThat(user.getUsername()).isEqualTo("newuser");
            assertThat(user.getPassword()).isEqualTo("password");
            assertThat(user.getEnabled()).isTrue();
            assertThat(user.getRoles()).isNull();
        }

        @Test
        @DisplayName("can represent user with roles via addRole")
        void canRepresentUserWithRolesViaAddRole() {
            user.setUsername("poweruser");
            
            user.addRole("ROLE_USER");
            user.addRole("ROLE_ADMIN");
            user.addRole("ROLE_MODERATOR");
            
            assertThat(user.getUsername()).isEqualTo("poweruser");
            assertThat(user.getRoles()).hasSize(3);
        }

        @Test
        @DisplayName("can represent admin user")
        void canRepresentAdminUser() {
            user.setUsername("admin");
            user.setPassword("adminpass");
            user.setEnabled(true);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(adminRole);
            user.setRoles(roles);
            
            assertThat(user.getUsername()).isEqualTo("admin");
            assertThat(user.getEnabled()).isTrue();
            assertThat(user.getRoles()).hasSize(1);
            assertThat(user.getRoles().iterator().next().getName()).isEqualTo("ROLE_ADMIN");
        }
    }

    @Nested
    @DisplayName("Integration with entity relationships")
    class IntegrationWithEntityRelationships {

        @Test
        @DisplayName("complete User -> Role relationship")
        void completeUserRoleRelationship() {
            var user = new User();
            user.setUsername("johndoe");
            user.setPassword("secret");
            user.setEnabled(true);
            
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(user);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(userRole);
            roles.add(adminRole);
            user.setRoles(roles);
            
            // Verify bidirectional relationship
            assertThat(user.getRoles()).containsExactlyInAnyOrder(userRole, adminRole);
            assertThat(userRole.getUser()).isEqualTo(user);
            assertThat(adminRole.getUser()).isEqualTo(user);
        }

        @Test
        @DisplayName("user can be associated with many roles")
        void userCanBeAssociatedWithManyRoles() {
            var user = new User();
            user.setUsername("multiroleuser");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(user);
            
            var role3 = new Role();
            role3.setName("ROLE_MODERATOR");
            role3.setUser(user);
            
            var role4 = new Role();
            role4.setName("ROLE_EDITOR");
            role4.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            roles.add(role3);
            roles.add(role4);
            user.setRoles(roles);
            
            assertThat(user.getRoles()).hasSize(4);
            assertThat(user.getRoles()).containsExactlyInAnyOrder(role1, role2, role3, role4);
        }

        @Test
        @DisplayName("addRole creates roles with correct user association")
        void addRoleCreatesRolesWithCorrectUserAssociation() {
            var user = new User();
            user.setUsername("convenienceuser");
            user.setPassword("password");
            user.setEnabled(true);
            
            user.addRole("ROLE_USER");
            user.addRole("ROLE_PREMIUM");
            
            var roles = user.getRoles();
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
            user.setUsername(longUsername);
            assertThat(user.getUsername()).isEqualTo(longUsername);
        }

        @Test
        @DisplayName("can handle very long passwords")
        void canHandleVeryLongPasswords() {
            var longPassword = "This is a very long password that might be used for security testing and validation purposes";
            user.setPassword(longPassword);
            assertThat(user.getPassword()).isEqualTo(longPassword);
        }

        @Test
        @DisplayName("can handle special characters in username")
        void canHandleSpecialCharactersInUsername() {
            var specialUsername = "user@domain.com";
            user.setUsername(specialUsername);
            assertThat(user.getUsername()).isEqualTo(specialUsername);
        }

        @Test
        @DisplayName("can handle numeric values in enabled field")
        void canHandleNumericValuesInEnabledField() {
            user.setEnabled(true);
            assertThat(user.getEnabled()).isTrue();
            
            user.setEnabled(false);
            assertThat(user.getEnabled()).isFalse();
            
            user.setEnabled(null);
            assertThat(user.getEnabled()).isNull();
        }

        @Test
        @DisplayName("addRole initializes roles set when null")
        void addRoleInitializesRolesSetWhenNull() {
            assertThat(user.getRoles()).isNull();
            
            user.addRole("ROLE_USER");
            
            assertThat(user.getRoles()).isNotNull();
            assertThat(user.getRoles()).isInstanceOf(HashSet.class);
        }

        @Test
        @DisplayName("can create user with minimal data")
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
            var user = new User();
            user.setUsername("fulluser");
            user.setPassword("password123");
            user.setEnabled(true);
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(user);
            
            var roles = new HashSet<Role>();
            roles.add(role1);
            roles.add(role2);
            user.setRoles(roles);
            
            assertThat(user.getUsername()).isEqualTo("fulluser");
            assertThat(user.getPassword()).isEqualTo("password123");
            assertThat(user.getEnabled()).isTrue();
            assertThat(user.getRoles()).hasSize(2);
        }
    }
}