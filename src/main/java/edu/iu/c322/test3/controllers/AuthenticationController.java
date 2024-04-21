package edu.iu.c322.test3.controllers;


import edu.iu.c322.test3.model.Customer;
import edu.iu.c322.test3.repository.CustomerRepository;
import edu.iu.c322.test3.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    CustomerRepository customerRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody Customer customer) {
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signin")
    public String login(@RequestBody Customer customer) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                customer.getUsername(),
                                customer.getPassword()));

        return tokenService.generateToken(authentication);
    }
}