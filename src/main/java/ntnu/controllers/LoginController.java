package ntnu.controllers;

import ntnu.dao.UserDAO;
import ntnu.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class LoginController {

    @Autowired
    private UserDAO userDAO;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User foundUser = userDAO.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (foundUser == null) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        } else {
            // create session or token to keep user authenticated
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        }
    }
}
