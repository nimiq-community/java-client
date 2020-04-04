package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on a transaction that is not yet sent.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutgoingTransaction {

    private String from;
    private Account.Type fromType;
    private String to;
    private Account.Type toType;
    private long value;
    private long fee;
    private String data;
    private int flags;

    /**
     * @return The address the transaction is send from.
     */
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return (optional, default: 0, Account.Type.BASIC) The account type at the
     *         given address (BASIC: 0, VESTING: 1, HTLC: 2).
     */
    public Account.Type getFromType() {
        return fromType;
    }

    public void setFromType(Account.Type fromType) {
        this.fromType = fromType;
    }

    /**
     * @return The address the transaction is directed to.
     */
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return (optional, default: 0, Account.Type.BASIC) The account type at the
     *         given address (BASIC: 0, VESTING: 1, HTLC: 2).
     */
    public Account.Type getToType() {
        return toType;
    }

    public void setToType(Account.Type toType) {
        this.toType = toType;
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

    @Override
    public String toString() {
        return "OutgoingTransaction [data=" + data + ", fee=" + fee + ", flags=" + flags + ", from=" + from
                + ", fromType=" + fromType + ", to=" + to + ", toType=" + toType + ", value=" + value + "]";
    }
}
