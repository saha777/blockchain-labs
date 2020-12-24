package org.izomp.transaction.manager.repository;

import org.izomp.transaction.manager.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query(value = "SELECT * " +
            "FROM {h-schema}blocks b, (SELECT MAX(b2.id) m FROM {h-schema}blocks b2) mb " +
            "WHERE b.id = mb.m", nativeQuery = true)
    Optional<Block> lastBlock();

    @Query("SELECT b " +
            "FROM Block as b " +
            "ORDER BY b.id")
    List<Block> findAllSorted();
}
