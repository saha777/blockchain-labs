package org.izomp.transaction.manager.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hash;
    private String previousHash;
    private UUID moderatorId;
    private String merkleRoot;
    private int nonce;
}
