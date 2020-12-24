package org.izomp.transaction.manager.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TransactionApprove {
    private List<UUID> responseIds;
    private UUID moderatorId;
}
