package org.izomp.transaction.manager.service;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.*;
import org.izomp.transaction.manager.repository.TransactionRepository;
import org.izomp.transaction.manager.repository.TransactionRequestRepository;
import org.izomp.transaction.manager.repository.TransactionResponseRepository;
import org.izomp.transaction.manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRequestRepository transactionRequestRepository;
    private final TransactionResponseRepository transactionResponseRepository;
    private final TransactionRepository transactionRepository;
    private final BlockchainService blockchainService;

    @Transactional
    public TransactionRequest createTransactionRequest(TransactionRequest transactionRequest) {
        User user = userRepository.getUserById(transactionRequest.getResidentId())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        if (user.getUserRole() != UserRole.RESIDENT) {
            throw new RuntimeException("USER_NOT_RESIDENT");
        }
        Optional<TransactionRequest> oldRequestOfThisUser = transactionRequestRepository.findByResidentId(transactionRequest.getResidentId());
        if (oldRequestOfThisUser.isPresent()) {
            throw new RuntimeException("REQUEST_ON_TRANSACTION_ALREADY_EXIST");
        }

        Transaction lastTransaction = transactionRepository.lastTransaction(transactionRequest.getResidentId())
                .orElseGet(() -> {
                    Transaction transaction = new Transaction();
                    transaction.setElectric(0);
                    transaction.setCache(0);
                    return transaction;
                });
        if (transactionRequest.getAction() == TransactionAction.BUY
                && transactionRequest.getPrice() > lastTransaction.getCache()) {
            throw new RuntimeException("NO_MONEY");
        } else if (transactionRequest.getAction() == TransactionAction.SELL
                && transactionRequest.getAmount() > lastTransaction.getElectric()) {
            throw new RuntimeException("NO_ELECTRIC");
        }
        transactionRequest.setId(UUID.randomUUID());
        return transactionRequestRepository.save(transactionRequest);
    }

    @Transactional
    public void cancelTransactionRequest(UUID requestId) {
        transactionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Transaction request absent"));
        transactionResponseRepository.deleteByRequestId(requestId);
        transactionRequestRepository.deleteById(requestId);
    }

    @Transactional
    public TransactionResponse createTransactionResponse(TransactionResponse transactionResponse) {
        TransactionRequest transactionRequest = transactionRequestRepository.findByIdAndActual(transactionResponse.getRequestId())
                .orElseThrow(() -> new RuntimeException("REQUEST_DOES_NOT_EXIST_OR_HAS_RESPONSE"));
        User user = userRepository.getUserById(transactionResponse.getRetailerId())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        if (user.getUserRole() != UserRole.RETAILER) {
            throw new RuntimeException("USER_NOT_RETAILER");
        }
        transactionResponse.setId(UUID.randomUUID());
        return transactionResponseRepository.save(transactionResponse);
    }

    @Transactional
    public void approveTransactions(TransactionApprove transactionApprove) {
        User user = userRepository.getUserById(transactionApprove.getModeratorId())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        if (user.getUserRole() != UserRole.MODERATOR) {
            throw new RuntimeException("USER_NOT_MODERATOR");
        }

        List<TransactionResponse> transactionResponseList = transactionResponseRepository.findAllById(transactionApprove.getResponseIds());
        List<UUID> transactionResponseIds = transactionResponseList.stream()
                .map(TransactionResponse::getId)
                .collect(Collectors.toList());
        List<UUID> transactionRequestIds = transactionResponseList.stream()
                .map(TransactionResponse::getRequestId)
                .collect(Collectors.toList());

        List<TransactionRequest> transactionRequestList =
                transactionRequestRepository.findAllById(transactionRequestIds);

        Map<UUID, Transaction> lastTransactions =
                transactionRepository.lastTransactions(transactionRequestIds)
                        .stream()
                .collect(Collectors.toMap(Transaction::getResidentId, t -> t));

        Transaction defaultTransaction = new Transaction();
        defaultTransaction.setCache(0);
        defaultTransaction.setElectric(0);

        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 0; i < transactionResponseList.size(); i++) {
            TransactionRequest request = transactionRequestList.get(i);
            TransactionResponse response = transactionResponseList.get(i);
            Transaction lastTransaction =
                    lastTransactions.getOrDefault(
                            request.getResidentId(),
                            defaultTransaction
                    );

            Transaction transaction = new Transaction();
            transaction.setTemporaryId(UUID.randomUUID());
            transaction.setAction(request.getAction());
            transaction.setAmount(request.getAmount());
            transaction.setPrice(request.getPrice());
            transaction.setResidentId(request.getResidentId());
            transaction.setRetailerId(response.getRetailerId());

            if (request.getAction() == TransactionAction.BUY) {
                transaction.setCache(lastTransaction.getCache() - request.getPrice());
                transaction.setElectric(lastTransaction.getElectric() + request.getAmount());
            } else if (request.getAction() == TransactionAction.SELL) {
                transaction.setCache(lastTransaction.getCache() + request.getPrice());
                transaction.setElectric(lastTransaction.getElectric() - request.getAmount());
            }

            transactionList.add(transaction);
        }

        Block block = blockchainService.mineBlock(transactionList, transactionApprove.getModeratorId());

        for (Transaction transaction : transactionList) {
            transaction.setBlockId(block.getId());
        }

        transactionRepository.saveAll(transactionList);
        transactionResponseRepository.deleteAllByIdIn(transactionResponseIds);
        transactionRequestRepository.deleteAllByIdIn(transactionRequestIds);
    }

}
