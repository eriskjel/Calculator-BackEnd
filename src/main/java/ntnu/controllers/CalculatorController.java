package ntnu.controllers;

import lombok.RequiredArgsConstructor;
import ntnu.auth.AuthenticationResponse;
import ntnu.auth.CalculationResponse;
import ntnu.models.User;
import ntnu.service.AuthenticationService;
import ntnu.service.CalculatorService;
import ntnu.models.Equation;
import ntnu.service.JwtService;
import ntnu.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    /*@PostMapping("/solve")
    public double solve(@RequestBody Equation equation){
        service.solve(equation);
        logger.info("Equation: n1: " + equation.getFactor1() +", n2: " +  equation.getFactor2()
                + ", operator: " + equation.getOperator());
        logger.info("Answer: " + service.getAnswer());

        if(service.addToLog(service.toString())){
            logger.info("Added to log: " + service.toString());
        }
        return service.getAnswer();
    }*/

    @PostMapping("/solve")
    public ResponseEntity<CalculationResponse> solve(@RequestBody Equation equation, @RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        System.out.println(token);
        User user = userDetailsService.findUserByUsername(jwtService.extractUsername(token));
        if(jwtService.isTokenValid(token, user)){
            calculatorService.solve(equation);

            logger.info("Equation: n1: " + equation.getFactor1() +", n2: " +  equation.getFactor2()
                    + ", operator: " + equation.getOperator());
            logger.info("Answer: " + calculatorService.getAnswer());
            CalculationResponse response = CalculationResponse.builder().result(calculatorService.getAnswer()).build();
            if(calculatorService.addToLog(calculatorService.toString())){
                logger.info("Added to log: " + calculatorService.toString());
            }
            return ResponseEntity.ok(response);
        }
        else{
            CalculationResponse response = CalculationResponse.builder().errorMessage("Invalid token").build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/log")
    @PreAuthorize("isAuthenticated()")
    public ArrayList<String> log(){
        logger.info("Returned log: " + calculatorService.toString());
        return calculatorService.getLog();
    }

}