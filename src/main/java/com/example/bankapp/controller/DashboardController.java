package com.example.bankapp.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.model.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.repository.UserRepository;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DashboardController(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Account account = accountRepository.findByUser(user)
                .orElseThrow();

        // ✅ FETCH ALL, ORDERED BY DATE DESC
        List<Transaction> recentTransactions =
                transactionRepository
                    .findByAccountOrderByTransactionDateDesc(account)
                    .stream()
                    .limit(5)   // ⭐ SHOW ONLY LAST 5
                    .toList();

        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("transactions", recentTransactions);

        return "dashboard";
    }
}
