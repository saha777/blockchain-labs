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
@Table(name = "transaction_responses")
public class TransactionResponse {
    @Id
    private UUID id;
    private UUID requestId;
    private UUID retailerId;
}
