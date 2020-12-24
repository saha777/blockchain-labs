package org.izomp.transaction.manager.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "transaction_requests")
public class TransactionRequest {
    @Id
    private UUID id;
    private UUID residentId;
    private TransactionAction action;
    private Integer price;
    private Integer amount;
}
