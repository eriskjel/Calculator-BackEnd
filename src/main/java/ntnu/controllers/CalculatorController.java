package ntnu.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntnu.auth.AuthenticationResponse;
import ntnu.auth.CalculationResponse;
import ntnu.enums.AuthenticationState;
import ntnu.exceptions.TokenExpiredException;
import ntnu.models.User;
import ntnu.service.AuthenticationService;
import ntnu.service.CalculatorService;
import ntnu.models.Equation;
import ntnu.service.JwtService;
import ntnu.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;


    Logger logger = LoggerFactory.getLogger(CalculatorController.class);


    @GetMapping("/ans")
    public double answer(){
        logger.info("Retrieved answer: " + calculatorService.getAnswer());
        return calculatorService.getAnswer();
    }

    @PostMapping("/solve")
    public ResponseEntity<CalculationResponse> solve(@RequestBody Equation equation, HttpServletRequest request){
        String token = extractTokenFromCookie(request);


        System.out.println(token);

        try {
            User user = userDetailsService.findUserByUsername(jwtService.extractUsername(token));
            AuthenticationState authState = jwtService.getAuthenticationState(token, user);

            if(authState == AuthenticationState.AUTHENTICATED){
                //calculatorService.solve(equation);

                calculatorService.saveCalculation(equation, user.getUsername());

                logger.info("Equation: n1: " + equation.getFactor1() +", n2: " +  equation.getFactor2()
                        + ", operator: " + equation.getOperator());
                logger.info("Answer: " + calculatorService.getAnswer());
                CalculationResponse response = CalculationResponse.builder()
                        .result(calculatorService.getAnswer())
                        .authenticationState(authState)
                        .build();
                if(calculatorService.addToLog(calculatorService.toString())){
                    logger.info("Added to log: " + calculatorService.toString());
                }
                return ResponseEntity.ok(response);
            }
            else {
                String errorMessage = "";
                if(authState == AuthenticationState.UNAUTHENTICATED){
                    errorMessage = "User is not authenticated";
                }
                CalculationResponse response = CalculationResponse.builder()
                        .errorMessage(errorMessage)
                        .authenticationState(authState)
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

        } catch (TokenExpiredException e) {
            CalculationResponse response = CalculationResponse.builder()
                    .errorMessage("Token is expired")
                    .authenticationState(AuthenticationState.TOKEN_EXPIRED)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/log")
    @PreAuthorize("isAuthenticated()")
    public ArrayList<String> log(){
        logger.info("Returned log: " + calculatorService.toString());
        return calculatorService.getLog();
    }

    @GetMapping("/allcalculations")
    public List<Equation> getAllCalculations(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        try{
            User user = userDetailsService.findUserByUsername(jwtService.extractUsername(token));
            AuthenticationState authState = jwtService.getAuthenticationState(token, user);
            System.out.println(authState);

            if (authState == AuthenticationState.UNAUTHENTICATED) {
                logger.info("User is not authenticated");
            }
            else if (authState == AuthenticationState.AUTHENTICATED) {
                return calculatorService.findAllCalculationsByUsername(user.getUsername());
            }
            else {
                logger.info("Unknown authentication state");
            }
        }catch (TokenExpiredException e){
            logger.info("Token is expired");
        }
        return null;
    }


    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}