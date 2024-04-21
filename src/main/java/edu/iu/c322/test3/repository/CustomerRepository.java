package edu.iu.c322.test3.repository;

import edu.iu.c322.test3.model.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerRepository {
    private static final Logger LOG =
            LoggerFactory.getLogger(CustomerRepository.class);

    private static final String DATABASE_NAME = "quizzes/customers.txt";
    private static final String NEW_LINE = System.lineSeparator();

    public CustomerRepository() {
        File file = new File(DATABASE_NAME);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private static void appendToFile(Path path, String content)
            throws IOException {
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public boolean save(Customer customer) throws Exception {
        Customer c = findByUsername(customer.getUsername());
        if(c != null) {
            return false;
        }
        Path path = Paths.get(DATABASE_NAME);
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String passwordEncoded = bc.encode(customer.getPassword());
        String data = customer.getUsername() + ","
                + passwordEncoded
                + "," + customer.getEmail;
        appendToFile(path, data + NEW_LINE);
        return true;
    }

    public List<Customer> findAll() throws IOException {
        List<Customer> result = new ArrayList<>();
        Path path = Paths.get(DATABASE_NAME);
        List<String> data = Files.readAllLines(path);
        for (String line : data) {
            if(!line.trim().isEmpty()) {
                String[] tokens = line.split(",");
                Customer c = new Customer(tokens[0], tokens[1], tokens[2]);
                result.add(c);
            }
        }
        return result;
    }

    public Customer findByUsername(String username) throws IOException {
        List<Customer> customers = findAll();
        for(Customer customer : customers) {
            if (customer.getUsername().trim().equalsIgnoreCase(username.trim())) {
                return customer;
            }
        }
        return null;
    }
}