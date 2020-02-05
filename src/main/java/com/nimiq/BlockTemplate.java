package com.nimiq;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A block template object.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockTemplate {

    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {

        private int version;
        private String prevHash;
        private String interlinkHash;
        private String accountsHash;
        private int nBits;
        private int height;

        /**
         * @return Version in block header.
         */
        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        /**
         * @return 32-byte hex-encoded hash of the previous block.
         */
        public String getPrevHash() {
            return prevHash;
        }

        public void setPrevHash(String prevHash) {
            this.prevHash = prevHash;
        }

        /**
         * @return 32-byte hex-encoded hash of the interlink.
         */
        public String getInterlinkHash() {
            return interlinkHash;
        }

        public void setInterlinkHash(String interlinkHash) {
            this.interlinkHash = interlinkHash;
        }

        /**
         * @return 32-byte hex-encoded hash of the accounts tree.
         */
        public String getAccountsHash() {
            return accountsHash;
        }

        public void setAccountsHash(String accountsHash) {
            this.accountsHash = accountsHash;
        }

        /**
         * @return Compact form of the hash target for this block.
         */
        public int getnBits() {
            return nBits;
        }

        public void setnBits(int nBits) {
            this.nBits = nBits;
        }

        /**
         * @return Height of the block in the block chain (also known as block number).
         */
        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "Header [accountsHash=" + accountsHash + ", height=" + height + ", interlinkHash=" + interlinkHash
                    + ", nBits=" + nBits + ", prevHash=" + prevHash + ", version=" + version + "]";
        }
    }

    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {

        private String hash;
        private String minerAddr;
        private String extraData;
        private String[] transactions;
        private String[] prunedAccounts;
        private String[] merkleHashes;

        /**
         * @return 32-byte hex-encoded hash of the block body.
         */
        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        /**
         * @return 20-byte hex-encoded miner address.
         */
        public String getMinerAddr() {
            return minerAddr;
        }

        public void setMinerAddr(String minerAddr) {
            this.minerAddr = minerAddr;
        }

        /**
         * @return Hex-encoded value of the extra data field.
         */
        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        /**
         * @return Array of hex-encoded transactions for this block.
         */
        public String[] getTransactions() {
            return transactions;
        }

        public void setTransactions(String[] transactions) {
            this.transactions = transactions;
        }

        /**
         * @return Array of hex-encoded pruned accounts for this block.
         */
        public String[] getPrunedAccounts() {
            return prunedAccounts;
        }

        public void setPrunedAccounts(String[] prunedAccounts) {
            this.prunedAccounts = prunedAccounts;
        }

        /**
         * @return Array of hex-encoded hashes that verify the path of the miner address
         *         in the merkle tree. This can be used to change the miner address
         *         easily.
         */
        public String[] getMerkleHashes() {
            return merkleHashes;
        }

        public void setMerkleHashes(String[] merkleHashes) {
            this.merkleHashes = merkleHashes;
        }

        @Override
        public String toString() {
            return "Body [extraData=" + extraData + ", hash=" + hash + ", merkleHashes=" + Arrays.toString(merkleHashes)
                    + ", minerAddr=" + minerAddr + ", prunedAccounts=" + Arrays.toString(prunedAccounts)
                    + ", transactions=" + Arrays.toString(transactions) + "]";
        }
    }

    private Header header;
    private String interlink;
    private Body body;
    private int target;

    /**
     * @return Block header.
     */
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * @return Hex-encoded block interlink.
     */
    public String getInterlink() {
        return interlink;
    }

    public void setInterlink(String interlink) {
        this.interlink = interlink;
    }

    /**
     * @return Block body.
     */
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    /**
     * @return Compact form of the hash target to submit a block to this client.
     */
    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "BlockTemplate [body=" + body + ", header=" + header + ", interlink=" + interlink + ", target=" + target
                + "]";
    }
}
