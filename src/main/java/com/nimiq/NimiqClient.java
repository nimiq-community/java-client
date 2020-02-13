package com.nimiq;

import java.util.List;

import com.googlecode.jsonrpc4j.JsonRpcMethod;

/**
 * This class allows to call RPC API of a Nimiq node.
 *
 * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API">JSON RPC API</a>
 * @see <a href="https://github.com/nimiq/core-js/blob/master/clients/nodejs/modules/JsonRpcServer.js">JsonRpcServer.js</a>
 */
public interface NimiqClient {

    // Network

    /**
     * Returns number of peers currently connected to the client.
     *
     * @return the number of connected peers.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#peerCount">peerCount</a>
     */
    @JsonRpcMethod("peerCount")
    public int getPeerCount();

    /**
     * Returns an object with data about the sync status.
     *
     * @return An object with sync status data
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#syncing">syncing</a>
     */
    @JsonRpcMethod("syncing")
    public SyncingStatus getSyncingStatus();

    /**
     * Returns information on the current consensus state. 
     *
     * @return String describing the consensus state. "established" is the value for
     *         a good state, other values indicate bad state
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#consensus">consensus</a>
     */
    @JsonRpcMethod("consensus")
    public ConsensusState getConsensusState();

    /**
     * Returns a list of peers known to the node.
     *
     * @return Array of peers
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#peerList">peerList</a>
     */
    @JsonRpcMethod("peerList")
    public List<PeerInfo> getPeerList();

    /**
     * Returns the state of the peer.
     *
     * @param address Address of the peer
     * @return The current peer state
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#peerState">peerState</a>
     */
    @JsonRpcMethod("peerState")
    public PeerInfo getPeerState(String address);

    /**
     * Changes the state of the peer.
     *
     * @param address Address of the peer
     * @param command One of "connect", "disconnect", "ban", "unban"
     * @return The new peer state
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#peerState">peerState</a>
     */
    @JsonRpcMethod("peerState")
    public PeerInfo setPeerState(String address, String command);

    // Transactions

    /**
     * Sends a signed message call transaction or a contract creation, if the data
     * field contains code.
     *
     * @param txHex The hex encoded signed transaction
     * @return the Hex-encoded transaction hash.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#sendRawTransaction">sendRawTransaction</a>
     */
    @JsonRpcMethod("sendRawTransaction")
    public String sendRawTransaction(String txHex);

    /**
     * Creates and signs a transaction without sending it. The transaction can then
     * be send via sendRawTransaction without accidentally replaying it.
     *
     * @param tx The transaction object
     * @return the Hex-encoded transaction.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#createRawTransaction">createRawTransaction</a>
     */
    @JsonRpcMethod("createRawTransaction")
    public String createRawTransaction(OutgoingTransaction tx);

    /**
     * Creates new message call transaction or a contract creation, if the data
     * field contains code.
     *
     * @param tx The transaction object
     * @return the Hex-encoded transaction hash.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#sendTransaction">sendTransaction</a>
     */
    @JsonRpcMethod("sendTransaction")
    public String sendTransaction(OutgoingTransaction tx);

    /**
     * Deserializes raw bytes and returns information about a transaction.
     *
     * @param txHex Hex-encoded presentation of a transaction
     * @return A transaction object
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getRawTransactionInfo">getRawTransactionInfo</a>
     */
    @JsonRpcMethod("getRawTransactionInfo")
    public Transaction getRawTransactionInfo(String txHex);

    /**
     * Returns information about a transaction by block hash and transaction index
     * position.
     *
     * @param hash  Hash of the block containing the transaction
     * @param index Index of the transaction in the block
     * @return A transaction object or null when no transaction was found.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionByBlockHashAndIndex">getTransactionByBlockHashAndIndex</a>
     */
    @JsonRpcMethod("getTransactionByBlockHashAndIndex")
    public Transaction getTransactionByBlockHashAndIndex(String hash, int index);

    /**
     * Returns information about a transaction by block number and transaction index
     * position.
     *
     * @param number Height of the block containing the transaction
     * @param index  Index of the transaction in the block
     * @return A transaction object or null when no transaction was found.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionByBlockNumberAndIndex">getTransactionByBlockNumberAndIndex</a>
     */
    @JsonRpcMethod("getTransactionByBlockNumberAndIndex")
    public Transaction getTransactionByBlockNumberAndIndex(int number, int index);

    /**
     * Returns the information about a transaction requested by transaction hash.
     *
     * @param hash Hash of a transaction
     * @return A transaction object or null when no transaction was found.
     * @throws FIXME It actually throws 'Unknown transaction hash' when no transaction was found.
     * 
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionByHash">getTransactionByHash</a>
     */
    @JsonRpcMethod("getTransactionByHash")
    public Transaction getTransactionByHash(String hash);

    /**
     * Returns the receipt of a transaction by transaction hash. Note that the
     * receipt is not available for pending transactions.
     *
     * @param hash Hash of a transaction
     * @return A transaction receipt object, or null when no receipt was found
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionReceipt">getTransactionReceipt</a>
     */
    @JsonRpcMethod("getTransactionReceipt")
    public TransactionReceipt getTransactionReceipt(String hash);

    /**
     * Returns the latest transactions successfully performed by or for an address.
     * That this information might change when blocks are rewinded on the local
     * state due to forks.
     *
     * @param address Address of which transactions should be gathered.
     * @return Array of transactions linked to the requested address. (up to 1000)
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionsByAddress">getTransactionsByAddress</a>
     */
    @JsonRpcMethod("getTransactionsByAddress")
    public List<Transaction> getTransactionsByAddress(String address);

    /**
     * Returns the latest transactions successfully performed by or for an address.
     * That this information might change when blocks are rewinded on the local
     * state due to forks.
     *
     * @param address Address of which transactions should be gathered.
     * @param limit   Maximum number of transactions that shall be returned.
     * @return Array of transactions linked to the requested address.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getTransactionsByAddress">getTransactionsByAddress</a>
     */
    @JsonRpcMethod("getTransactionsByAddress")
    public List<Transaction> getTransactionsByAddress(String address, int limit);

    /**
     * Returns transactions that are currently in the mempool.
     *
     * @param includeTransactions If true it returns the full transaction objects,
     *                            if false only the hashes of the transactions.
     * @return Array of transactions. Either represented by the transaction hash or
     *         a Transaction object.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#mempoolContent">mempoolContent</a>
     */
    @JsonRpcMethod("mempoolContent")
    public List<Transaction> getMempoolContent(boolean includeTransactions);

    /**
     * Returns information on the current mempool situation. This will provide an
     * overview of the number of transactions sorted into buckets based on their fee
     * per byte (in smallest unit).
     *
     * @return Mempool information
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#mempool">mempool</a>
     */
    @JsonRpcMethod("mempool")
    public Mempool getMempool();

    /**
     * Gets the current minimum fee per byte.
     *
     * @return The current value
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#minFeePerByte">minFeePerByte</a>
     */
    @JsonRpcMethod("minFeePerByte")
    public long getMinFeePerByte();

    /**
     * Sets the minimum fee per byte.
     *
     * @param minFeePerByte The new value
     * @return The new value
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#minFeePerByte">minFeePerByte</a>
     */
    @JsonRpcMethod("minFeePerByte")
    public long setMinFeePerByte(long minFeePerByte);

    // Miner

    /**
     * Returns true if client is actively mining new blocks.
     *
     * @return true if the client is mining, otherwise false.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#mining">mining</a>
     */
    @JsonRpcMethod("mining")
    public boolean isMining();

    /**
     * Enables or disables the miner.
     *
     * @param enabled true to start the miner
     * @return true if the client is mining, otherwise false.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#mining">mining</a>
     */
    @JsonRpcMethod("mining")
    public boolean setMining(boolean enabled);

    /**
     * Returns the number of hashes per second that the node is mining with.
     *
     * @return number of hashes per second.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#hashrate">hashrate</a>
     */
    @JsonRpcMethod("hashrate")
    public int getHashrate();

    /**
     * Returns the number of CPU threads the miner is using.
     *
     * @return the current number of threads
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#minerThreads">minerThreads</a>
     */
    @JsonRpcMethod("minerThreads")
    public int getMinerThreads();

    /**
     * Sets the number of CPU threads the miner is using.
     *
     * @param threads number of threads
     * @return the new number of threads
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#minerThreads">minerThreads</a>
     */
    @JsonRpcMethod("minerThreads")
    public int setMinerThreads(int threads);

    /**
     * Returns the user friendly miner address.
     *
     * @return the miner address
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#minerAddress">minerAddress</a>
     */
    @JsonRpcMethod("minerAddress")
    public String getMinerAddress();

    /**
     * Returns the current pool address.
     *
     * @return pool address or null
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#pool">pool</a>
     */
    @JsonRpcMethod("pool")
    public String getPoolAddress();

    /**
     * Set the new pool to switch to.
     *
     * @param pool pool address
     * @return the new pool address
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#pool">pool</a>
     */
    @JsonRpcMethod("pool")
    public String setPoolAddress(String pool);

    /**
     * Returns the pool connection state.
     *
     * @return the connection state: 0 - connected, 1 - connecting, 2 - closed
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#poolConnectionState">poolConnectionState</a>
     */
    @JsonRpcMethod("poolConnectionState")
    public int getPoolConnectionState();

    /**
     * Returns the miner balance confirmed by the pool.
     *
     * @return the balance (in smallest unit)
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#poolConfirmedBalance">poolConfirmedBalance</a>
     */
    @JsonRpcMethod("poolConfirmedBalance")
    public long getPoolConfirmedBalance();

    /**
     * Returns instructions to mine the next block. This will consider pool
     * instructions when connected to a pool.
     *
     * @param address   The address to use as a miner for this block. This overrides
     *                  the address provided during startup or from the pool.
     * @param extraData Hex-encoded value for the extra data field. This overrides
     *                  the address provided during startup or from the pool.
     * @return Mining work instructions
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getWork">getWork</a>
     */
    @JsonRpcMethod("getWork")
    public Work getWork(String address, String extraData);

    /**
     * Returns a template to build the next block for mining. This will consider
     * pool instructions when connected to a pool.
     *
     * @param address   The address to use as a miner for this block. This overrides
     *                  the address provided during startup or from the pool.
     * @param extraData Hex-encoded value for the extra data field. This overrides
     *                  the address provided during startup or from the pool.
     * @return A block template object.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBlockTemplate">getBlockTemplate</a>
     */
    @JsonRpcMethod("getBlockTemplate")
    public BlockTemplate getBlockTemplate(String address, String extraData);

    /**
     * Submits a block to the node. When the block is valid, the node will forward
     * it to other nodes in the network.
     *
     * @param blockHex Hex-encoded full block (including header, interlink and
     *                 body). When submitting work from getWork, remember to include
     *                 the suffix.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#submitBlock">submitBlock</a>
     */
    @JsonRpcMethod("submitBlock")
    public void submitBlock(String blockHex);

    // Accounts

    /**
     * Returns a list of addresses owned by client.
     *
     * @return array of accounts owned by the client.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#accounts">accounts</a>
     */
    @JsonRpcMethod("accounts")
    public List<Account> getAccounts();

    /**
     * Creates a new account and stores its private key in the client store.
     *
     * @return Information on the wallet that was created using the command.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#createAccount">createAccount</a>
     */
    @JsonRpcMethod("createAccount")
    public Wallet createAccount();

    /**
     * Returns the balance of the account of given address.
     *
     * @param address Address to check for balance.
     * @return The current balance at the specified address (in smallest unit).
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBalance">getBalance</a>
     */
    @JsonRpcMethod("getBalance")
    public long getBalance(String address);

    /**
     * Returns details for the account of given address.
     *
     * @param address Address of the account.
     * @return Details about the account. Returns the default empty basic account
     *         for non-existing accounts.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getAccount">getAccount</a>
     */
    @JsonRpcMethod("getAccount")
    public Account getAccount(String address);

    // Blockchain

    /**
     * Returns the height of most recent block.
     *
     * @return The current block height the client is on.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#blockNumber">blockNumber</a>
     */
    @JsonRpcMethod("blockNumber")
    public int getBlockNumber();

    /**
     * Returns the number of transactions in a block from a block matching the given
     * block hash.
     *
     * @param hash Hash of the block.
     * @return Number of transactions in the block found, or null, when no block was
     *         found.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBlockTransactionCountByHash">getBlockTransactionCountByHash</a>
     */
    @JsonRpcMethod("getBlockTransactionCountByHash")
    public Integer getBlockTransactionCountByHash(String hash);

    /**
     * Returns the number of transactions in a block matching the given block
     * number.
     *
     * @param number Height of the block.
     * @return Number of transactions in the block found, or null, when no block was
     *         found.
     * @throws FIXME It actually throws 'Invalid height' when no block was found.
     * 
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBlockTransactionCountByNumber">getBlockTransactionCountByNumber</a>
     */
    @JsonRpcMethod("getBlockTransactionCountByNumber")
    public Integer getBlockTransactionCountByNumber(int number);

    /**
     * Returns information about a block by hash.
     *
     * @param hash                Hash of the block to gather information on.
     * @param includeTransactions If true it returns the full transaction objects,
     *                            if false only the hashes of the transactions.
     * @return A block object or null when no block was found.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBlockByHash">getBlockByHash</a>
     */
    @JsonRpcMethod("getBlockByHash")
    public Block getBlockByHash(String hash, boolean includeTransactions);

    /**
     * Returns information about a block by block number.
     *
     * @param number              The height of the block to gather information on.
     * @param includeTransactions If true it returns the full transaction objects,
     *                            if false only the hashes of the transactions.
     * @return A block object or null when no block was found.
     * @throws FIXME It actually throws 'Invalid height' when no block was found.
     * 
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#getBlockByNumber">getBlockByNumber</a>
     */
    @JsonRpcMethod("getBlockByNumber")
    public Block getBlockByNumber(int number, boolean includeTransactions); // TODO: latest, latest-N

    // Misc

    /**
     * Gets the value of the numerical constant.
     *
     * @param name The name of the constant
     * @return The current value of the constant
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#constant">constant</a>
     */
    @JsonRpcMethod("constant")
    public long getConstant(String name);

    /**
     * Sets the value of the numerical constant.
     *
     * @param name  The name of the constant
     * @param value Either a numerical value or "reset" to set a default value
     * @return The new value of the constant
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#constant">constant</a>
     */
    @JsonRpcMethod("constant")
    public long setConstant(String name, Object value);

    /**
     * Sets the log level of the node.
     *
     * @param tag   If '*' the log level is set globally, otherwise the log level is
     *              applied only on this tag.
     * @param level Minimum log level to display. (Valid options: trace, verbose,
     *              debug, info, warn, error, assert)
     * @return True if the log level was set.
     *
     * @see <a href="https://github.com/nimiq/core-js/wiki/JSON-RPC-API#log">log</a>
     */
    @JsonRpcMethod("log")
    public boolean setLogLevel(String tag, String level);

}
