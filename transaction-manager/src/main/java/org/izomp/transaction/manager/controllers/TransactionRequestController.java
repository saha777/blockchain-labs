package org.izomp.transaction.manager.controllers;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.TransactionRequest;
import org.izomp.transaction.manager.repository.TransactionRequestRepository;
import org.izomp.transaction.manager.security.CurrentUser;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.izomp.transaction.manager.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction-requests")
@RequiredArgsConstructor
public class TransactionRequestController {

    private final TransactionService transactionService;
    private final TransactionRequestRepository transactionRequestRepository;

    @GetMapping
    public ResponseEntity<List<TransactionRequest>> findAll() {
        return ResponseEntity.ok(transactionRequestRepository.findAllActual());
    }

    @Secured("ROLE_RESIDENT")
    @PostMapping
    public ResponseEntity<TransactionRequest> createTransactionRequest(@RequestBody TransactionRequest request, @CurrentUser UserPrincipal userPrincipal) {
        request.setResidentId(userPrincipal.getId());
        return ResponseEntity.ok(transactionService.createTransactionRequest(request));
    }

    @Secured("ROLE_RESIDENT")
    @DeleteMapping("/{requestId}")
    public ResponseEntity<TransactionRequest> cancel(@PathVariable("requestId") UUID requestId) {
        transactionService.cancelTransactionRequest(requestId);
        return ResponseEntity.noContent().build();
    }

}
