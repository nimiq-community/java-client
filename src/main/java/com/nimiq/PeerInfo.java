package com.nimiq;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on a network peer.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerInfo {

    public enum AddressState {
        NEW(1), ESTABLISHED(2), TRIED(3), FAILED(4), BANNED(5);

        private int number;

        AddressState(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        @JsonCreator
        public static AddressState fromNumber(int number) {
            for (AddressState state : AddressState.values()) {
                if (state.getNumber() == number) {
                    return state;
                }
            }
            return null;
        }
    }

    public enum ConnectionState {
        NEW(1), CONNECTING(2), CONNECTED(3), NEGOTIATING(4), ESTABLISHED(5), CLOSED(6);

        private int number;

        ConnectionState(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        @JsonCreator
        public static ConnectionState fromNumber(int number) {
            for (ConnectionState state : ConnectionState.values()) {
                if (state.getNumber() == number) {
                    return state;
                }
            }
            return null;
        }
    }

    private String id;
    private String address;
    private AddressState addressState;
    private ConnectionState connectionState;
    private int version;
    private long timeOffset;
    private String headHash;
    private long latency;
    private long rx;
    private long tx;

    /**
     * @return Peer id.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Peer address. (URL)
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Address state.
     */
    public AddressState getAddressState() {
        return addressState;
    }

    public void setAddressState(AddressState addressState) {
        this.addressState = addressState;
    }

    /**
     * @return Connection state.
     */
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    /**
     * @return Version.
     */
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return Time offset. (in milliseconds)
     */
    public long getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * @return Hash of the head block the peer is on.
     */
    public String getHeadHash() {
        return headHash;
    }

    public void setHeadHash(String headHash) {
        this.headHash = headHash;
    }

    /**
     * @return Network latency. (in milliseconds)
     */
    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    /**
     * @return Bytes received.
     */
    public long getRx() {
        return rx;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }

    /**
     * @return Bytes sent.
     */
    public long getTx() {
        return tx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

    @Override
    public String toString() {
        return "PeerInfo [address=" + address + ", addressState=" + addressState + ", connectionState="
                + connectionState + ", headHash=" + headHash + ", id=" + id + ", latency=" + latency + ", rx=" + rx
                + ", timeOffset=" + timeOffset + ", tx=" + tx + ", version=" + version + "]";
    }
}
