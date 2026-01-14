package com.example.bankapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.model.TransactionType;
import com.example.bankapp.model.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.repository.UserRepository;

@Controller
public class TransactionController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionController(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // ---------------- VIEW TRANSACTIONS ----------------
    @GetMapping("/transactions")
    public String transactions(Authentication auth, Model model) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Account account = accountRepository.findByUser(user)
                .orElseThrow();

        List<Transaction> transactions =
                transactionRepository.findByAccountOrderByTransactionDateDesc(account);

        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    // ---------------- DEPOSIT ----------------
    @PostMapping("/deposit")
    public String deposit(
            @RequestParam double amount,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Account account = accountRepository.findByUser(user)
                .orElseThrow();

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setTransactionType(TransactionType.DEPOSIT);
        tx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(tx);

        redirectAttributes.addFlashAttribute("success", "Deposit successful");
        return "redirect:/dashboard";
    }

    // ---------------- WITHDRAW ----------------
    @PostMapping("/withdraw")
    public String withdraw(
            @RequestParam double amount,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Account account = accountRepository.findByUser(user)
                .orElseThrow();

        if (account.getBalance() < amount) {
            redirectAttributes.addFlashAttribute("error", "Insufficient balance");
            return "redirect:/dashboard";
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setTransactionType(TransactionType.WITHDRAW);
        tx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(tx);

        redirectAttributes.addFlashAttribute("success", "Withdraw successful");
        return "redirect:/dashboard";
    }

    // ---------------- TRANSFER ----------------
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String toUsername,
            @RequestParam double amount,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        // 1️⃣ Logged-in (sender) user
        User senderUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // 2️⃣ Receiver user
        User receiverUser = userRepository.findByUsername(toUsername)
                .orElse(null);

        if (receiverUser == null) {
            redirectAttributes.addFlashAttribute("error", "Recipient not found");
            return "redirect:/dashboard";
        }

        // 3️⃣ Fetch accounts
        Account senderAccount = accountRepository.findByUser(senderUser)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiverAccount = accountRepository.findByUser(receiverUser)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // 4️⃣ Validation
        if (senderAccount.getBalance() < amount) {
            redirectAttributes.addFlashAttribute("error", "Insufficient balance");
            return "redirect:/dashboard";
        }

        if (amount <= 0) {
            redirectAttributes.addFlashAttribute("error", "Invalid transfer amount");
            return "redirect:/dashboard";
        }

        // 5️⃣ Update balances
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 6️⃣ Sender transaction (DEBIT)
        Transaction senderTx = new Transaction();
        senderTx.setAccount(senderAccount);
        senderTx.setTransactionType(TransactionType.TRANSFER_OUT);
        senderTx.setAmount(amount);
        senderTx.setDescription(
                "Transfer sent to " + receiverAccount.getAccountNumber()
        );
        senderTx.setTransactionDate(LocalDateTime.now());

        // 7️⃣ Receiver transaction (CREDIT)
        Transaction receiverTx = new Transaction();
        receiverTx.setAccount(receiverAccount);
        receiverTx.setTransactionType(TransactionType.TRANSFER_IN);
        receiverTx.setAmount(amount);
        receiverTx.setDescription(
                "Transfer received from " + senderAccount.getAccountNumber()
        );
        receiverTx.setTransactionDate(LocalDateTime.now());

        // 8️⃣ Save both transactions
        transactionRepository.save(senderTx);
        transactionRepository.save(receiverTx);

        redirectAttributes.addFlashAttribute("success", "Transfer successful");
        return "redirect:/dashboard";
    }

}