package org.izomp.transaction.manager.controllers;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/block-chain")
@RequiredArgsConstructor
public class BlockChainController {

    private final BlockchainService blockchainService;

    @GetMapping("/valid")
    public ResponseEntity<Boolean> isBlockChainValid() {
        return ResponseEntity.ok(blockchainService.isBlockChainValid());
    }
}
