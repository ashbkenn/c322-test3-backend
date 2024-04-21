package edu.iu.c322.test3.service;

import edu.iu.c322.test3.model.Customer;

import java.io.IOException;

public interface IAuthenticationService {
    Customer signup(Customer customer) throws IOException;
    boolean login(String username, String password) throws IOException;
}