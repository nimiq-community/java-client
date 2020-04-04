
package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on a transaction.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private String hash;
    private String blockHash;
    private int blockNumber;
    private int timestamp;
    private int confirmations;
    private int transactionIndex;
    private String from;
    private String fromAddress;
    private String to;
    private String toAddress;
    private long value;
    private long fee;
    private String data;
    private int flags;

    private boolean valid = true;
    private boolean inMempool;

    public Transaction() {
    }

    public Transaction(String hash) {
        this.hash = hash;
    }

    /**
     * @return Hex-encoded hash of the transaction.
     */
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return Hex-encoded hash of the block containing the transaction.
     */
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * @return Height of the block containing the transaction.
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return UNIX timestamp of the block containing the transaction.
     */
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return Number of confirmations of the block containing the transaction.
     */
    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    /**
     * @return Index of the transaction in the block.
     */
    public int getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(int transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    /**
     * @return Hex-encoded address of the sending account.
     */
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return Nimiq user friendly address (NQ-address) of the sending account.
     */
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return Hex-encoded address of the recipient account.
     */
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return Nimiq user friendly address (NQ-address) of the recipient account.
     */
    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * @return Integer of the value (in smallest unit) sent with this transaction.
     */
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    /**
     * @return Integer of the fee (in smallest unit) for this transaction.
     */
    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    /**
     * @return Hex-encoded contract parameters or a message.
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return Bit-encoded transaction flags.
     */
    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @return Whether this transaction has been successfully verified and found
     *         valid. <b>Note:</b> This field only makes sense when the Transaction
     *         object was returned by
     *         {@link NimiqClient#getRawTransactionInfo(String)}
     */
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return Whether this transaction is currently in the mempool. <b>Note:</b>
     *         This field only makes sense when the Transaction object was returned
     *         by {@link NimiqClient#getRawTransactionInfo(String)}
     */
    public boolean isInMempool() {
        return inMempool;
    }

    public void setInMempool(boolean inMempool) {
        this.inMempool = inMempool;
    }

    @Override
    public String toString() {
        return "Transaction [blockHash=" + blockHash + ", blockNumber=" + blockNumber + ", confirmations="
                + confirmations + ", data=" + data + ", fee=" + fee + ", flags=" + flags + ", from=" + from
                + ", fromAddress=" + fromAddress + ", hash=" + hash + ", timestamp=" + timestamp + ", to=" + to
                + ", toAddress=" + toAddress + ", transactionIndex=" + transactionIndex + ", value=" + value + "]";
    }
}
