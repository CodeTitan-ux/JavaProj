package com.example.bankapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        AccountRepository accountRepository,
        TransactionRepository transactionRepository,
        PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
public void registerUser(String username, String password) {

    if (userRepository.findByUsername(username).isPresent()) {
        throw new RuntimeException("Username already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole("USER");

    userRepository.save(user);

    Account account = new Account();
    account.setUser(user);
    account.setAccountNumber(generateAccountNumber());
    account.setBalance(0.0);
    account.setAccountType("SAVINGS");

    accountRepository.save(account);
}


    private String generateAccountNumber() {
        return "GC-" + System.currentTimeMillis();
    }

}