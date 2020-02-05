package com.nimiq;

import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

/**
 * Factory class that can create instances of {@link NimiqClient}.
 */
public class NimiqClientFactory {

    private final JsonRpcHttpClient jsonRpcClient;

    /**
     * Creates the {@link NimiqClientFactory} class for the given URL.
     *
     * @param url The URL of the RPC service
     */
    public NimiqClientFactory(URL url) {
        jsonRpcClient = new JsonRpcHttpClient(url);
    }

    /**
     * Creates the {@link NimiqClientFactory} class for the given URL and
     * credentials.
     *
     * @param url      The URL of the RPC service
     * @param userName The name of the user
     * @param password The password
     */
    public NimiqClientFactory(URL url, String userName, String password) {
        String credentials = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
        Map<String, String> headers = Collections.singletonMap("Authorization", "Basic " + credentials);
        jsonRpcClient = new JsonRpcHttpClient(url, headers);
    }

    /**
     * Create the {@link NimiqClient} class.
     *
     * @return The client instance
     */
    public NimiqClient getClient() {
        return ProxyUtil.createClientProxy(NimiqClient.class.getClassLoader(), NimiqClient.class, jsonRpcClient);
    }
}
