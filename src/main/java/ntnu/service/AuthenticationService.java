package ntnu.service;


import lombok.RequiredArgsConstructor;
import ntnu.auth.AuthenticationRequest;
import ntnu.auth.AuthenticationResponse;
import ntnu.auth.RegisterRequest;
import ntnu.exceptions.InvalidCredentialsException;
import ntnu.exceptions.UserAlreadyExistsException;
import ntnu.models.Role;
import ntnu.models.User;
import ntnu.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        if (repository.findByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username already exists");
        repository.save(user);
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws InvalidCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwToken).build();
    }


}
