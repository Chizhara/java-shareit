package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {

    private final EntityManager em;

    private final UserService userService;
    private static User[] usersTestData;
    private static User userTestData;


    @BeforeAll
    public static void initUsersTestData() {
        usersTestData = TestData.getUsers();
        userTestData = User.builder().id(3L).name("User C").email("anothermail@email.com").build();
    }

    @Test
    public void shouldReturnUser1ById1() {
        User user = userService.getUser(1L);
        assertThat(user, notNullValue());
        assertThat(user, equalTo(usersTestData[0]));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetUserByInvalidId() {
        assertThrows(NotFoundException.class, () -> userService.getUser(-1));
    }

    @Test
    public void shouldReturnCorrectUserList() {
        Collection<User> users = userService.getUsers();

        assertThat(users, notNullValue());
        assertThat(users.size(), equalTo(2));
        assertThat(users, contains(usersTestData));
    }

    @Test
    public void shouldSaveUserWithCorrectId() {
        userService.addUser(userTestData);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE email = :email", User.class);
        User user = query.setParameter("email", userTestData.getEmail()).getSingleResult();
        assertThat(user, notNullValue());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userTestData.getId()));
        assertThat(user.getName(), equalTo(userTestData.getName()));
        assertThat(user.getEmail(), equalTo(userTestData.getEmail()));
        userTestData.setId(userTestData.getId() + 1L);
    }

    @Test
    public void shouldDeleteUserById() {
        userService.addUser(userTestData);
        userService.deleteUser(userTestData.getId());
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u ", User.class);
        List<User> users = query.getResultList();
        assertThat(users, not(contains(userTestData)));
        userTestData.setId(userTestData.getId() + 1L);
    }

    @Test
    public void shouldUpdateUserFieldsById() {
        userService.addUser(userTestData);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE email = :email", User.class);

        userTestData.setName("Updated User C");
        userTestData.setEmail("updatedmail@mail.com");
        userService.updateUser(userTestData, userTestData.getId());
        User user = query.setParameter("email", userTestData.getEmail()).getSingleResult();
        assertThat(user, notNullValue());
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userTestData.getId()));
        assertThat(user.getName(), equalTo(userTestData.getName()));
        assertThat(user.getEmail(), equalTo(userTestData.getEmail()));
        userService.deleteUser(userTestData.getId());
        userTestData.setId(userTestData.getId() + 1L);
    }

    @Test
    public void shouldValidateUserByIdWhenCorrect() {
        assertDoesNotThrow(() -> userService.validateUserExistById(1));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenValidateUserByInvalidId() {
        assertThrows(NotFoundException.class, () -> userService.validateUserExistById(-1));
    }

}
