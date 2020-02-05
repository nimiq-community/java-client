package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A transaction receipt object.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionReceipt {

    private String transactionHash;
    private int transactionIndex;
    private String blockHash;
    private int blockNumber;
    private int confirmations;
    private int timestamp;

    /**
     * @return Hex-encoded hash of the transaction.
     */
    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    /**
     * @return The transactions index position in the block.
     */
    public int getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(int transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    /**
     * @return Hex-encoded hash of the block where this transaction was in.
     */
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * @return Block number where this transaction was in.
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return Number of confirmations for this transaction (number of blocks on top
     *         of the block where this transaction was in).
     */
    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    /**
     * @return Timestamp of the block where this transaction was in.
     */
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TransactionReceipt [blockHash=" + blockHash + ", blockNumber=" + blockNumber + ", confirmations="
                + confirmations + ", timestamp=" + timestamp + ", transactionHash=" + transactionHash
                + ", transactionIndex=" + transactionIndex + "]";
    }
}
