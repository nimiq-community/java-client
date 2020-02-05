package com.nimiq;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on a block.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    private int number;
    private String hash;
    private String pow;
    private String parentHash;
    private int nonce;
    private String bodyHash;
    private String accountHash;
    private String miner;
    private String minerAddress;
    private String difficulty;
    private String extraData;
    private int size;
    private int timestamp;
    private int confirmations;
    private List<Transaction> transactions;

    /**
     * @return Height of the block.
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return Hex-encoded 32-byte hash of the block.
     */
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return Hex-encoded 32-byte Proof-of-Work hash of the block.
     */
    public String getPow() {
        return pow;
    }

    public void setPow(String pow) {
        this.pow = pow;
    }

    /**
     * @return Hex-encoded 32-byte hash of the predecessor block.
     */
    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    /**
     * @return The nonce of the block used to fulfill the Proof-of-Work.
     */
    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    /**
     * @return Hex-encoded 32-byte hash of the block body merkle root.
     */
    public String getBodyHash() {
        return bodyHash;
    }

    public void setBodyHash(String bodyHash) {
        this.bodyHash = bodyHash;
    }

    /**
     * @return Hex-encoded 32-byte hash of the accounts tree root.
     */
    public String getAccountHash() {
        return accountHash;
    }

    public void setAccountHash(String accountHash) {
        this.accountHash = accountHash;
    }

    /**
     * @return Hex-encoded 20 byte address of the miner of the block.
     */
    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    /**
     * @return User friendly address (NQ-address) of the miner of the block.
     */
    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    /**
     * @return Block difficulty, encoded as decimal number in string.
     */
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return Hex-encoded value of the extra data field, maximum of 255 bytes.
     */
    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    /**
     * @return Block size in bytes.
     */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return UNIX timestamp of the block.
     */
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Number of confirmations of the block.
     */
    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    /**
     * @return Array of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Block [accountHash=" + accountHash + ", bodyHash=" + bodyHash + ", confirmations=" + confirmations
                + ", difficulty=" + difficulty + ", extraData=" + extraData + ", hash=" + hash + ", miner=" + miner
                + ", minerAddress=" + minerAddress + ", nonce=" + nonce + ", number=" + number + ", parentHash="
                + parentHash + ", pow=" + pow + ", size=" + size + ", timestamp=" + timestamp + ", transactions="
                + transactions + "]";
    }
}
