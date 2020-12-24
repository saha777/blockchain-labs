package org.izomp.transaction.manager.repository;

import org.izomp.transaction.manager.entities.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, UUID> {
    @Query(value = "SELECT * " +
            "FROM {h-schema}transaction_requests tr " +
            "WHERE tr.id = :requestId AND " +
            "(SELECT count(*) FROM {h-schema}transaction_responses res WHERE res.request_id = :requestId) = 0", nativeQuery = true)
    Optional<TransactionRequest> findByIdAndActual(@Param("requestId") UUID requestId);
    Optional<TransactionRequest> findByResidentId(UUID requestById);

    @Query(value = "SELECT * " +
            "FROM {h-schema}transaction_requests tr " +
            "WHERE (SELECT count(*) FROM {h-schema}transaction_responses res WHERE res.request_id = tr.id) = 0", nativeQuery = true)
    List<TransactionRequest> findAllActual();

    @Modifying
    @Query(value = "DELETE FROM {h-schema}transaction_requests t WHERE t.id IN(:ids)", nativeQuery = true)
    void deleteAllByIdIn(@Param("ids") List<UUID> transactionRequestIds);
}
