package org.izomp.transaction.manager.repository;

import org.izomp.transaction.manager.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * " +
            "FROM {h-schema}transactions t, (SELECT MAX(t2.id) m FROM {h-schema}transactions as t2 WHERE t2.resident_id = :residentId) mt " +
            "WHERE t.id = mt.m", nativeQuery = true)
    Optional<Transaction> lastTransaction(@Param("residentId") UUID residentId);

    @Query(value = "SELECT * " +
            "FROM {h-schema}transactions t LEFT JOIN (SELECT t2.resident_id, MAX(t2.id) m " +
            "FROM {h-schema}transactions t2 " +
            "GROUP BY t2.resident_id) mt ON mt.resident_id = t.resident_id " +
            "WHERE t.resident_id IN (:residentIds) AND t.id = mt.m", nativeQuery = true)
    List<Transaction> lastTransactions(@Param("residentIds") List<UUID> residentIds);

    @Query("SELECT t " +
            "FROM Transaction as t " +
            "WHERE t.residentId = :residentId " +
            "ORDER BY t.id")
    List<Transaction> findAllByResidentId(@Param("residentId") UUID residentId);
}
