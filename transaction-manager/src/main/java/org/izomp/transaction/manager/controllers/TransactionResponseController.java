package org.izomp.transaction.manager.controllers;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.TransactionApprove;
import org.izomp.transaction.manager.entities.TransactionResponse;
import org.izomp.transaction.manager.repository.TransactionResponseRepository;
import org.izomp.transaction.manager.security.CurrentUser;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.izomp.transaction.manager.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-responses")
@RequiredArgsConstructor
public class TransactionResponseController {
    private final TransactionService transactionService;
    private final TransactionResponseRepository transactionResponseRepository;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAll() {
        return ResponseEntity.ok(transactionResponseRepository.findAll());
    }

    @Secured("ROLE_RETAILER")
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransactionResponse(@RequestBody TransactionResponse response, @CurrentUser UserPrincipal userPrincipal) {
        response.setRetailerId(userPrincipal.getId());
        return ResponseEntity.ok(transactionService.createTransactionResponse(response));
    }

    @Secured("ROLE_MODERATOR")
    @PostMapping("/approve")
    public ResponseEntity<TransactionResponse> approveTransactionResponses(@RequestBody TransactionApprove approve, @CurrentUser UserPrincipal userPrincipal) {
        approve.setModeratorId(userPrincipal.getId());
        transactionService.approveTransactions(approve);
        return ResponseEntity.noContent().build();
    }
}
