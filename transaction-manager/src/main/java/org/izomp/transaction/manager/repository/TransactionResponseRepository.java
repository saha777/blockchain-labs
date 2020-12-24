package org.izomp.transaction.manager.repository;

import org.izomp.transaction.manager.entities.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionResponseRepository extends JpaRepository<TransactionResponse, UUID> {
    @Modifying
    void deleteByRequestId(UUID requestId);
    @Modifying
    @Query(value = "DELETE FROM {h-schema}transaction_responses t WHERE t.id IN(:ids)", nativeQuery = true)
    void deleteAllByIdIn(@Param("ids") List<UUID> transactionResponseIds);
}
