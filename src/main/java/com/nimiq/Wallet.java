package com.nimiq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Details on a wallet.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet {

    private String id;
    private String address;
    private String publicKey;
    private String privateKey;

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
     * @return Hex-encoded 32 byte Ed25519 public key.
     */
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return Hex-encoded 32 byte Ed25519 private key.
     */
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "Wallet [address=" + address + ", id=" + id + ", privateKey=" + privateKey + ", publicKey=" + publicKey
                + "]";
    }
}
