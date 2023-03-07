package ntnu.dao;

import ntnu.models.User;

import java.util.List;

public interface UserDAO {
    int saveUser(User user);
    User findUserById(int id);

    List<User> getAllUsers();
}
