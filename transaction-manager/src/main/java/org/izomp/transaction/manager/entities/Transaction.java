package org.izomp.transaction.manager.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID temporaryId;
    private Long blockId;
    private Integer cache;
    private Integer electric;
    private UUID residentId;
    private UUID retailerId;
    private TransactionAction action;
    private Integer price;
    private Integer amount;
}
