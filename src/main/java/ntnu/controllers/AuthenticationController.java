package ntnu.controllers;


import lombok.RequiredArgsConstructor;
import ntnu.auth.AuthenticationRequest;
import ntnu.auth.AuthenticationResponse;
import ntnu.auth.RegisterRequest;
import ntnu.exceptions.InvalidCredentialsException;
import ntnu.exceptions.UserAlreadyExistsException;
import ntnu.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {


    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder().errorMessage(e.getMessage()).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder().errorMessage(e.getMessage()).build());
        }
    }

}
