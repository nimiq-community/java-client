package com.nimiq;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Consensus state.
 */
public enum ConsensusState {

    /**
     * The client is connecting to the network.
     */
    CONNECTING,

    /**
     * The client is syncing data from peers to reach consensus.
     */
    SYNCING,

    /**
     * The client reached consensus with its peers.
     */
    ESTABLISHED;

    @JsonCreator
    public static ConsensusState fromString(String str) {
        for (ConsensusState state : ConsensusState.values()) {
            if (state.name().equalsIgnoreCase(str)) {
                return state;
            }
        }
        return null;
    }
}
