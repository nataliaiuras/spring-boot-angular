package com.example.repository;

import com.example.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE " +
            "(:accountId IS NULL OR t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId) AND " +
            "((cast(:startDate as date) IS NULL) OR t.transactionDate >= :startDate) AND " +
            "((cast(:endDate as date) IS NULL) OR t.transactionDate <= :endDate) AND " +
            "(:type IS NULL OR t.operationType = :type) AND " +
            "(:status IS NULL OR t.status = :status)")
    Page<Transaction> findTransactionsByAccount(
            @Param("accountId") Long accountId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("type") String type,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(t.sourceAmount), 0) FROM Transaction t " +
            "WHERE (t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId) " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.operationType IN ('TRANSFER_INTERNAL', 'TRANSFER_EXTERNAL') " +
            "AND t.status = 'COMPLETED'")
    Optional<BigDecimal> sumTransferAmountByAccountAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );


    @Query("SELECT t FROM Transaction t WHERE " +
            "(:accountId IS NULL OR t.sourceAccount.id = :accountId OR t.destinationAccount.id = :accountId) AND " +
            "((cast(:startDate as date) is NULL) OR t.transactionDate >= :startDate) AND " +
            "((cast(:endDate as date ) IS NULL) OR t.transactionDate <= :endDate)")
    Page<Transaction> findTransactionsPeriodByAccount(
            @Param("accountId") Long accountId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );
}
