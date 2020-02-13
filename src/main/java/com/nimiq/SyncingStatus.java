package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Synchronization status.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncingStatus {

    private boolean syncing;
    private int startingBlock;
    private int currentBlock;
    private int highestBlock;

    public SyncingStatus() {
        this.syncing = true;
    }

    public SyncingStatus(boolean syncing) {
        this.syncing = syncing;
    }

    /**
     * @return Whether the node is syncing with the network.
     */
    public boolean isSyncing() {
        return syncing;
    }

    public void setSyncing(boolean syncing) {
        this.syncing = syncing;
    }

    /**
     * @return The block at which the import started. (will only be reset, after the
     *         sync reached his head)
     */
    public int getStartingBlock() {
        return startingBlock;
    }

    public void setStartingBlock(int startingBlock) {
        this.startingBlock = startingBlock;
    }

    /**
     * @return The current block, same as blockNumber.
     */
    public int getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(int currentBlock) {
        this.currentBlock = currentBlock;
    }

    /**
     * @return The estimated highest block.
     */
    public int getHighestBlock() {
        return highestBlock;
    }

    public void setHighestBlock(int highestBlock) {
        this.highestBlock = highestBlock;
    }

    @Override
    public String toString() {
        return "SyncingStatus [currentBlock=" + currentBlock + ", highestBlock=" + highestBlock + ", startingBlock="
                + startingBlock + ", syncing=" + syncing + "]";
    }
}
