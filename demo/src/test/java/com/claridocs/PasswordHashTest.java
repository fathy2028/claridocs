package com.claridocs;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashTest {

    @Test
    public void generatePasswordHash() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String plainPassword = "password";
        String hashedPassword = passwordEncoder.encode(plainPassword);
        
        System.out.println("Plain password: " + plainPassword);
        System.out.println("Hashed password: " + hashedPassword);
        
        // Test if the hash matches
        boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
        System.out.println("Password matches: " + matches);
        
        // Test with the hash you used in SQL
        String sqlHash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        boolean sqlMatches = passwordEncoder.matches(plainPassword, sqlHash);
        System.out.println("SQL hash matches 'password': " + sqlMatches);
    }
}
