package ntnu.dao;

import ntnu.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserDAOImplTest {

    @Autowired
    private UserDAOImpl userDaoImpl;



    @AfterEach
    public void tearDown() {
        userDaoImpl.deleteAllUsers();
    }

    @Test
    @DisplayName("Test that a user can be saved to the database, and that the user can be found by id")
    public void testSaveUser_testFindUserByID() {
        User user = new User(0, "john", "password");
        userDaoImpl.saveUser(user);

        int userId = user.getId();
        assertTrue(userId > 0); // Check that the id was updated after save

        User foundUser = userDaoImpl.findUserById(userId);
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("Test that it is possible to delete and find all users")
    public void testDeleteAllUsers_FindAllUsers() {
        // Create and save some users
        User user1 = new User(0, "john", "password");
        User user2 = new User(0, "jane", "password");
        userDaoImpl.saveUser(user1);
        userDaoImpl.saveUser(user2);

        // Ensure there are initially two users in the database
        List<User> usersBefore = userDaoImpl.getAllUsers();
        assertEquals(2, usersBefore.size());

        // Call deleteAllUsers() method
        userDaoImpl.deleteAllUsers();

        // Ensure that there are no users in the database
        List<User> usersAfter = userDaoImpl.getAllUsers();
        assertEquals(0, usersAfter.size());
    }

}
