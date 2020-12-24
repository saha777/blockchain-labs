package org.izomp.transaction.manager.service;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.Block;
import org.izomp.transaction.manager.entities.Transaction;
import org.izomp.transaction.manager.repository.BlockRepository;
import org.izomp.transaction.manager.service.utils.HashUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private static final int DIFFICULTY = 4;

    private final BlockRepository blockRepository;

    @Transactional
    public Block mineBlock(List<Transaction> transactions, UUID moderatorId) {
        int nonce = 0;

        Block lastBlock = blockRepository.lastBlock()
                .orElseGet(() -> {
                    Block block = new Block();
                    block.setHash("NULL");
                    return block;
                });

        Block block = new Block();
        block.setPreviousHash(lastBlock.getHash());
        block.setMerkleRoot(HashUtils.getMerkleRoot(transactions));
        block.setModeratorId(moderatorId);
        block.setNonce(nonce);

        String hash = calculateHash(block);
        String target = new String(new char[DIFFICULTY]).replace('\0', '0');
        while (!hash.startsWith(target)) {
            nonce++;
            block.setNonce(nonce);
            hash = calculateHash(block);
        }
        block.setHash(hash);

        return blockRepository.save(block);
    }

    private String calculateHash(Block block) {
        return HashUtils.applySha256(block.getPreviousHash() + block.getMerkleRoot() + block.getNonce() + block.getModeratorId().toString());
    }

    public Boolean isBlockChainValid() {
        boolean isValid = true;
        List<Block> blocks = blockRepository.findAllSorted();
        for (int i = 0; i < blocks.size() - 1; i++) {
            if (!blocks.get(i).getHash().equals(blocks.get(i + 1).getPreviousHash())) {
                isValid = false;
                break;
            }
            if (!blocks.get(i).getHash().equals(calculateHash(blocks.get(i)))) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}
