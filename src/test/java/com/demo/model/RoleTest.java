package com.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Nested
    @DisplayName("Name field")
    class NameField {

        @Test
        @DisplayName("name is null by default")
        void nameIsNullByDefault() {
            assertThat(role.getName()).isNull();
        }

        @Test
        @DisplayName("setName sets the name")
        void setNameSetsName() {
            role.setName("ROLE_ADMIN");
            assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
        }

        @Test
        @DisplayName("setName overwrites previous name")
        void setNameOverwritesPreviousName() {
            role.setName("ROLE_USER");
            role.setName("ROLE_ADMIN");
            assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
        }

        @Test
        @DisplayName("setName to null resets name")
        void setNameToNull() {
            role.setName("ROLE_USER");
            role.setName(null);
            assertThat(role.getName()).isNull();
        }

        @Test
        @DisplayName("can set empty name")
        void canSetEmptyName() {
            role.setName("");
            assertThat(role.getName()).isEmpty();
        }

        @Test
        @DisplayName("has @Column annotation with name role")
        void hasColumnAnnotationWithNameRole() throws Exception {
            var nameField = Role.class.getDeclaredField("name");
            var columnAnnotation = nameField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("role");
        }
    }

    @Nested
    @DisplayName("User relationship (ManyToOne)")
    class UserRelationship {

        @Test
        @DisplayName("user is null by default")
        void userIsNullByDefault() {
            assertThat(role.getUser()).isNull();
        }

        @Test
        @DisplayName("setUser sets the user")
        void setUserSetsUser() {
            var user = new User();
            user.setUsername("johndoe");
            
            role.setUser(user);
            
            assertThat(role.getUser()).isEqualTo(user);
        }

        @Test
        @DisplayName("setUser overwrites previous user")
        void setUserOverwritesPreviousUser() {
            var user1 = new User();
            user1.setUsername("user1");
            var user2 = new User();
            user2.setUsername("user2");
            
            role.setUser(user1);
            role.setUser(user2);
            
            assertThat(role.getUser()).isEqualTo(user2);
        }

        @Test
        @DisplayName("setUser to null resets user")
        void setUserToNull() {
            var user = new User();
            user.setUsername("johndoe");
            
            role.setUser(user);
            role.setUser(null);
            
            assertThat(role.getUser()).isNull();
        }

        @Test
        @DisplayName("has @ManyToOne relationship")
        void hasManyToOneRelationship() throws Exception {
            var userField = Role.class.getDeclaredField("user");
            var manyToOneAnnotation = userField.getAnnotation(ManyToOne.class);
            assertThat(manyToOneAnnotation).isNotNull();
        }

        @Test
        @DisplayName("has @JoinColumn with name username")
        void hasJoinColumnWithNameUsername() throws Exception {
            var userField = Role.class.getDeclaredField("user");
            var joinColumnAnnotation = userField.getAnnotation(JoinColumn.class);
            assertThat(joinColumnAnnotation).isNotNull();
            assertThat(joinColumnAnnotation.name()).isEqualTo("username");
        }

        @Test
        @DisplayName("user field is annotated with @JsonIgnore")
        void userFieldIsAnnotatedWithJsonIgnore() throws Exception {
            var userField = Role.class.getDeclaredField("user");
            assertThat(userField.getAnnotation(JsonIgnore.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(Role.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name roles")
        void hasTableAnnotationWithNameRoles() {
            var tableAnnotation = Role.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("roles");
        }

        @Test
        @DisplayName("has @Table annotation with unique constraint on username and role")
        void hasTableAnnotationWithUniqueConstraint() {
            var tableAnnotation = Role.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.uniqueConstraints()).hasSize(1);
            
            var uniqueConstraint = tableAnnotation.uniqueConstraints()[0];
            assertThat(uniqueConstraint.columnNames()).containsExactly("username", "role");
        }

        @Test
        @DisplayName("extends BaseEntity")
        void extendsBaseEntity() {
            assertThat(role).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(Role.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Role.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta relationship imports")
        void usesJakartaRelationshipImports() throws Exception {
            var userField = Role.class.getDeclaredField("user");
            assertThat(userField.getAnnotation(ManyToOne.class)).isNotNull();
            assertThat(userField.getAnnotation(JoinColumn.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta UniqueConstraint import")
        void usesJakartaUniqueConstraintImport() {
            var tableAnnotation = Role.class.getAnnotation(Table.class);
            assertThat(tableAnnotation.uniqueConstraints()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Entity lifecycle from inheritance")
    class EntityLifecycleFromInheritance {

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(role.getId()).isNull();
            role.setId(42);
            assertThat(role.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(role.isNew()).isTrue();
            role.setId(1);
            assertThat(role.isNew()).isFalse();
        }

        @Test
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            role.setName("ROLE_ADMIN");
            assertThat(role.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            role.setId(1);
            assertThat(role.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Bidirectional relationship with User")
    class BidirectionalRelationshipWithUser {

        @Test
        @DisplayName("role can be associated with user")
        void roleCanBeAssociatedWithUser() {
            var user = new User();
            user.setUsername("admin");
            
            role.setName("ROLE_ADMIN");
            role.setUser(user);
            
            assertThat(role.getUser()).isEqualTo(user);
            assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
        }

        @Test
        @DisplayName("multiple roles can be associated with same user")
        void multipleRolesCanBeAssociatedWithSameUser() {
            var user = new User();
            user.setUsername("johndoe");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(user);
            
            assertThat(role1.getUser()).isEqualTo(role2.getUser());
            assertThat(role1.getUser().getUsername()).isEqualTo("johndoe");
            assertThat(role2.getUser().getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("role can be moved from one user to another")
        void roleCanBeMovedFromOneUserToAnother() {
            var user1 = new User();
            user1.setUsername("user1");
            var user2 = new User();
            user2.setUsername("user2");
            
            role.setName("ROLE_USER");
            role.setUser(user1);
            assertThat(role.getUser()).isEqualTo(user1);
            
            role.setUser(user2);
            assertThat(role.getUser()).isEqualTo(user2);
        }

        @Test
        @DisplayName("role can exist without user")
        void roleCanExistWithoutUser() {
            role.setName("ROLE_USER");
            assertThat(role.getName()).isEqualTo("ROLE_USER");
            assertThat(role.getUser()).isNull();
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can represent common security roles")
        void canRepresentCommonSecurityRoles() {
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            assertThat(adminRole.getName()).isEqualTo("ROLE_ADMIN");

            var userRole = new Role();
            userRole.setName("ROLE_USER");
            assertThat(userRole.getName()).isEqualTo("ROLE_USER");

            var moderatorRole = new Role();
            moderatorRole.setName("ROLE_MODERATOR");
            assertThat(moderatorRole.getName()).isEqualTo("ROLE_MODERATOR");
        }

        @Test
        @DisplayName("can handle role names with different cases")
        void canHandleRoleNamesWithDifferentCases() {
            var role1 = new Role();
            role1.setName("admin");
            assertThat(role1.getName()).isEqualTo("admin");

            var role2 = new Role();
            role2.setName("ADMIN");
            assertThat(role2.getName()).isEqualTo("ADMIN");

            var role3 = new Role();
            role3.setName("Admin");
            assertThat(role3.getName()).isEqualTo("Admin");
        }

        @Test
        @DisplayName("can handle special characters in role names")
        void canHandleSpecialCharactersInRoleNames() {
            var role1 = new Role();
            role1.setName("ROLE_USER_1");
            assertThat(role1.getName()).isEqualTo("ROLE_USER_1");

            var role2 = new Role();
            role2.setName("role-with-hyphens");
            assertThat(role2.getName()).isEqualTo("role-with-hyphens");
        }

        @Test
        @DisplayName("is new when id is null regardless of role name")
        void isNewWhenIdIsNullRegardlessOfRoleName() {
            role.setName("ROLE_ADMIN");
            assertThat(role.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if role name is null")
        void isNotNewWhenIdIsSetEvenIfRoleNameIsNull() {
            role.setId(1);
            assertThat(role.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Integration with User entity")
    class IntegrationWithUserEntity {

        @Test
        @DisplayName("can be used in User's roles collection")
        void canBeUsedInUsersRolesCollection() throws Exception {
            var user = new User();
            user.setUsername("admin");
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(user);
            
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(user);
            
            // User has OneToMany relationship with Role
            var rolesField = User.class.getDeclaredField("roles");
            var oneToManyAnnotation = rolesField.getAnnotation(jakarta.persistence.OneToMany.class);
            
            assertThat(oneToManyAnnotation).isNotNull();
            assertThat(oneToManyAnnotation.mappedBy()).isEqualTo("user");
        }

        @Test
        @DisplayName("multiple roles can belong to same user")
        void multipleRolesCanBelongToSameUser() {
            var user = new User();
            user.setUsername("johndoe");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_ADMIN");
            role2.setUser(user);
            
            var role3 = new Role();
            role3.setName("ROLE_MODERATOR");
            role3.setUser(user);
            
            // All roles should reference the same user
            assertThat(role1.getUser()).isSameAs(role2.getUser());
            assertThat(role2.getUser()).isSameAs(role3.getUser());
            assertThat(role3.getUser().getUsername()).isEqualTo("johndoe");
        }

        @Test
        @DisplayName("user can have different role combinations")
        void userCanHaveDifferentRoleCombinations() {
            var admin = new User();
            admin.setUsername("admin");
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(admin);
            
            var regularUser = new User();
            regularUser.setUsername("user");
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(regularUser);
            
            var moderatorUser = new User();
            moderatorUser.setUsername("moderator");
            var modRole = new Role();
            modRole.setName("ROLE_MODERATOR");
            modRole.setUser(moderatorUser);
            
            assertThat(adminRole.getUser()).isNotEqualTo(userRole.getUser());
            assertThat(userRole.getUser()).isNotEqualTo(modRole.getUser());
        }
    }

    @Nested
    @DisplayName("Database constraints")
    class DatabaseConstraints {

        @Test
        @DisplayName("unique constraint prevents duplicate username-role combinations")
        void uniqueConstraintPreventsDuplicateUsernameRoleCombinations() {
            var user = new User();
            user.setUsername("johndoe");
            
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user);
            
            var role2 = new Role();
            role2.setName("ROLE_USER");
            role2.setUser(user);
            
            // Both roles would have same username and role, violating unique constraint
            // This test verifies the constraint is defined
            var tableAnnotation = Role.class.getAnnotation(Table.class);
            assertThat(tableAnnotation.uniqueConstraints()).hasSize(1);
            
            var constraint = tableAnnotation.uniqueConstraints()[0];
            assertThat(constraint.columnNames()).containsExactly("username", "role");
        }

        @Test
        @DisplayName("allows same role name for different users")
        void allowsSameRoleNameForDifferentUsers() {
            var user1 = new User();
            user1.setUsername("user1");
            var role1 = new Role();
            role1.setName("ROLE_USER");
            role1.setUser(user1);
            
            var user2 = new User();
            user2.setUsername("user2");
            var role2 = new Role();
            role2.setName("ROLE_USER");
            role2.setUser(user2);
            
            // Same role name is allowed for different users
            assertThat(role1.getName()).isEqualTo(role2.getName());
            assertThat(role1.getUser()).isNotEqualTo(role2.getUser());
            assertThat(role1.getUser().getUsername()).isNotEqualTo(role2.getUser().getUsername());
        }

        @Test
        @DisplayName("allows different role names for same user")
        void allowsDifferentRoleNamesForSameUser() {
            var user = new User();
            user.setUsername("johndoe");
            
            var userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setUser(user);
            
            var adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(user);
            
            // Different role names are allowed for same user
            assertThat(userRole.getName()).isNotEqualTo(adminRole.getName());
            assertThat(userRole.getUser()).isEqualTo(adminRole.getUser());
        }
    }

    @Nested
    @DisplayName("JSON serialization considerations")
    class JsonSerializationConsiderations {

        @Test
        @DisplayName("user field is ignored during JSON serialization")
        void userFieldIsIgnoredDuringJsonSerialization() throws Exception {
            var userField = Role.class.getDeclaredField("user");
            assertThat(userField.getAnnotation(JsonIgnore.class)).isNotNull();
        }

        @Test
        @DisplayName("name field is included in JSON serialization")
        void nameFieldIsIncludedInJsonSerialization() throws Exception {
            var nameField = Role.class.getDeclaredField("name");
            assertThat(nameField.getAnnotation(JsonIgnore.class)).isNull();
        }

        @Test
        @DisplayName("id field is included in JSON serialization")
        void idFieldIsIncludedInJsonSerialization() throws Exception {
            var idField = BaseEntity.class.getDeclaredField("id");
            assertThat(idField.getAnnotation(JsonIgnore.class)).isNull();
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("can handle very long role names")
        void canHandleVeryLongRoleNames() {
            var longRoleName = "ROLE_VERY_LONG_ADMINISTRATIVE_ROLE_NAME_WITH_MANY_PRIVILEGES";
            role.setName(longRoleName);
            assertThat(role.getName()).isEqualTo(longRoleName);
        }

        @Test
        @DisplayName("can handle role names with numbers")
        void canHandleRoleNamesWithNumbers() {
            role.setName("ROLE_USER_123");
            assertThat(role.getName()).isEqualTo("ROLE_USER_123");
        }

        @Test
        @DisplayName("can handle role names with underscores and hyphens")
        void canHandleRoleNamesWithUnderscoresAndHyphens() {
            var role1 = new Role();
            role1.setName("ROLE_USER_ADMIN");
            assertThat(role1.getName()).isEqualTo("ROLE_USER_ADMIN");

            var role2 = new Role();
            role2.setName("role-with-hyphens");
            assertThat(role2.getName()).isEqualTo("role-with-hyphens");
        }

        @Test
        @DisplayName("can be created with minimal data")
        void canBeCreatedWithMinimalData() {
            var minimalRole = new Role();
            assertThat(minimalRole.getId()).isNull();
            assertThat(minimalRole.getName()).isNull();
            assertThat(minimalRole.getUser()).isNull();
            assertThat(minimalRole.isNew()).isTrue();
        }

        @Test
        @DisplayName("can be fully populated")
        void canBeFullyPopulated() {
            var user = new User();
            user.setUsername("admin");
            
            var fullRole = new Role();
            fullRole.setId(100);
            fullRole.setName("ROLE_ADMIN");
            fullRole.setUser(user);
            
            assertThat(fullRole.getId()).isEqualTo(100);
            assertThat(fullRole.getName()).isEqualTo("ROLE_ADMIN");
            assertThat(fullRole.getUser()).isEqualTo(user);
            assertThat(fullRole.isNew()).isFalse();
        }
    }
}