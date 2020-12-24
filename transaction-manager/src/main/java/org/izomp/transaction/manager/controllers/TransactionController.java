package org.izomp.transaction.manager.controllers;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.Transaction;
import org.izomp.transaction.manager.repository.TransactionRepository;
import org.izomp.transaction.manager.security.CurrentUser;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping
    public List<Transaction> findAll(@CurrentUser UserPrincipal userPrincipal) {
        return transactionRepository.findAllByResidentId(userPrincipal.getId());
    }

    @GetMapping("/last")
    public Transaction findLast(@CurrentUser UserPrincipal userPrincipal) {
        return transactionRepository.lastTransaction(userPrincipal.getId()).orElseThrow(() -> new RuntimeException("TRANSACTION_NOT_FOUND"));
    }


}
