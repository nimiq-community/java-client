package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on an account.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    public enum Type {
        BASIC, VESTING, HTLC;

        @JsonValue
        public int toValue() {
            return ordinal();
        }
    }

    private String id;
    private String address;
    private long balance;
    private Type type;
    // Vesting contract fields
    private String owner;
    private String ownerAddress;
    private int vestingStart;
    private int vestingStepBlocks;
    private long vestingStepAmount;
    private long vestingTotalAmount;
    // HTLC fields
    private String sender;
    private String senderAddress;
    private String recipient;
    private String recipientAddress;
    private String hashRoot;
    private int hashCount;
    private int timeout;
    private long totalAmount;

    /**
     * @return Hex-encoded 20 byte address.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return User friendly address (NQ-address).
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Balance of the account (in smallest unit).
     */
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     * @return The account type associated with the account (BASIC: 0, VESTING: 1,
     *         HTLC: 2).
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return Hex-encoded 20 byte address of the owner of the vesting contract.
     */
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return User friendly address (NQ-address) of the owner of the vesting
     *         contract.
     */
    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    /**
     * @return The block that the vesting contracted commenced.
     */
    public int getVestingStart() {
        return vestingStart;
    }

    public void setVestingStart(int vestingStart) {
        this.vestingStart = vestingStart;
    }

    /**
     * @return The number of blocks after which some part of the vested funds is
     *         released.
     */
    public int getVestingStepBlocks() {
        return vestingStepBlocks;
    }

    public void setVestingStepBlocks(int vestingStepBlocks) {
        this.vestingStepBlocks = vestingStepBlocks;
    }

    /**
     * @return The amount (in smallest unit) released every vestingStepBlocks
     *         blocks.
     */
    public long getVestingStepAmount() {
        return vestingStepAmount;
    }

    public void setVestingStepAmount(long vestingStepAmount) {
        this.vestingStepAmount = vestingStepAmount;
    }

    /**
     * @return The total amount (in smallest unit) that was provided at the contract
     *         creation.
     */
    public long getVestingTotalAmount() {
        return vestingTotalAmount;
    }

    public void setVestingTotalAmount(long vestingTotalAmount) {
        this.vestingTotalAmount = vestingTotalAmount;
    }

    /**
     * @return Hex-encoded 20 byte address of the sender of the HTLC.
     */
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return User friendly address (NQ-address) of the sender of the HTLC.
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * @return Hex-encoded 20 byte address of the recipient of the HTLC.
     */
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * @return User friendly address (NQ-address) of the recipient of the HTLC.
     */
    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    /**
     * @return Hex-encoded 32 byte hash root.
     */
    public String getHashRoot() {
        return hashRoot;
    }

    public void setHashRoot(String hashRoot) {
        this.hashRoot = hashRoot;
    }

    /**
     * @return Number of hashes this HTLC is split into.
     */
    public int getHashCount() {
        return hashCount;
    }

    public void setHashCount(int hashCount) {
        this.hashCount = hashCount;
    }

    /**
     * @return Block after which the contract can only be used by the original
     *         sender to recover funds.
     */
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return The total amount (in smallest unit) that was provided at the contract
     *         creation.
     */
    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Account [address=" + address + ", balance=" + balance + ", id=" + id + ", type=" + type + "]";
    }
}
