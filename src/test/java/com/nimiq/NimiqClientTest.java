
package com.nimiq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * NimiqClientTest<br/>
 * Assumes a Nimiq node is started locally:
 *
 * <pre>
 * nodejs index.js --protocol=dumb --type=full --network=test --rpc
 * </pre>
 *
 * requres a test account charged with some NIM
 */
public class NimiqClientTest {

    private static final String RPC_URL = "http://localhost:8648/";

    private static final String NULL_ADDRESS = "NQ07 0000 0000 0000 0000 0000 0000 0000 0000";
    private static final String UNKNOWN_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    private static final int TX_SIZE = 138;
    private static final long MIN_FEE = TX_SIZE; // 1 luna/byte

    private static NimiqClient client;

    private static Account testAccount;
    private static Block testBlock;

    @BeforeClass
    public static void createClient() throws IOException {
        final URL url = new URL(RPC_URL);
        client = new NimiqClientFactory(url).getClient();

        // Choose the sender account
        testAccount = client.getAccounts().stream()
                .filter(account -> account.getBalance() > 0)
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Can't find account with enough balance"));

        // Find some block with multiple transactions
        final int headBlock = client.getBlockNumber();
        testBlock = IntStream.range(0, headBlock)
                .mapToObj(i -> client.getBlockByNumber(headBlock - i, true))
                .filter(block -> block.getTransactions().size() > 1)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Can't find non-empty block"));
    }

    // Network

    @Test
    public void testGetPeerCount() {
        assertTrue(client.getPeerCount() > 0);
    }

    @Test
    public void testGetSyncingState() {
        final SyncingState state = client.getSyncingState();
        if (state.isSyncing()) {
            assertTrue(state.getHighestBlock() > 0);
        }
    }

    @Test
    public void testGetConsensusState() {
        assertNotNull(client.getConsensusState());
    }

    @Test
    public void testGetPeerList() {
        final List<PeerInfo> peers = client.getPeerList();
        assertFalse(peers.isEmpty());
        peers.forEach(peer -> {
            assertNotNull(peer.getId());
            assertNotNull(peer.getAddress());
            if (peer.getAddressState() == PeerInfo.AddressState.ESTABLISHED) {
                assertEquals(PeerInfo.ConnectionState.ESTABLISHED, peer.getConnectionState());
                assertNotNull(peer.getHeadHash());
                assertTrue(peer.getBytesSent() > 0);
                assertTrue(peer.getBytesReceived() > 0);
            }
        });
    }

    @Test
    public void testGetPeerState() {
        client.getPeerList().forEach(peer -> {
            final PeerInfo state = client.getPeerState(peer.getAddress());
            assertEquals(peer.getId(), state.getId());
            assertEquals(peer.getAddress(), state.getAddress());
            assertEquals(peer.getAddressState(), state.getAddressState());
        });
    }

    @Test
    public void testSetPeerState() {
        // try to connect to all new nodes
        client.getPeerList().stream()
                .filter(peer -> peer.getAddressState() == PeerInfo.AddressState.NEW)
                .forEach(peer -> {
                    final PeerInfo state = client.setPeerState(peer.getAddress(), "connect");
                    assertNotNull(state.getConnectionState());
                });
    }

    // Transactions

    @Test
    public void testSendRawTransaction() {
        final OutgoingTransaction outTx = createTransaction(testAccount.getAddress(), NULL_ADDRESS, 13_00000, MIN_FEE);

        final String txHash = client.sendRawTransaction(client.createRawTransaction(outTx));
        assertNotNull(txHash);
        assertNotNull(client.getTransactionByHash(txHash));
    }

    @Test
    public void testCreateRawTransaction() {
        final OutgoingTransaction outTx = createTransaction(testAccount.getAddress(), NULL_ADDRESS, 12_00000, MIN_FEE); // No real spending

        final String txHex = client.createRawTransaction(outTx);
        assertNotNull(txHex);
        assertEquals(TX_SIZE, txHex.length() / 2);
    }

    @Test
    public void testSendTransaction() {
        final OutgoingTransaction outTx = createTransaction(testAccount.getAddress(), NULL_ADDRESS, 10_00000, MIN_FEE);

        final String txHash = client.sendTransaction(outTx);
        assertNotNull(txHash);
        assertNotNull(client.getTransactionByHash(txHash));
    }

    @Test
    public void testGetRawTransactionInfo() {
        final OutgoingTransaction outTx = createTransaction(testAccount.getAddress(), NULL_ADDRESS, 8_00000, MIN_FEE);
        final String txHex = client.createRawTransaction(outTx);

        final Transaction txBefore = client.getRawTransactionInfo(txHex);
        assertNotNull(txBefore.getHash());
        assertEquals(testAccount.getAddress(), txBefore.getFromAddress());
        assertEquals(NULL_ADDRESS, txBefore.getToAddress());
        assertEquals(8_00000, txBefore.getValue());
        assertEquals(MIN_FEE, txBefore.getFee());
        assertTrue(txBefore.isValid());
        assertFalse(txBefore.isInMempool());

        final String txHash = client.sendRawTransaction(txHex);
        final Transaction txAfter = client.getRawTransactionInfo(txHex);
        assertEquals(txHash, txAfter.getHash());
        assertTrue(txAfter.isValid());
        assertTrue(txAfter.isInMempool());
    }

    @Test
    public void testGetTransactionByBlockHashAndIndex() {
        assertNull(client.getTransactionByBlockHashAndIndex(UNKNOWN_HASH, 0));
        assertNull(client.getTransactionByBlockHashAndIndex(testBlock.getHash(), 999));

        final List<Transaction> txs = testBlock.getTransactions();
        for (int idx = 0; idx > txs.size(); idx++) {
            final Transaction tx = client.getTransactionByBlockHashAndIndex(testBlock.getHash(), idx);
            assertEquals(txs.get(idx).getHash(), tx.getHash());
        }
    }

    @Test
    public void testGetTransactionByBlockNumberAndIndex() {
        assertNull(client.getTransactionByBlockNumberAndIndex(testBlock.getNumber(), 999));

        final List<Transaction> txs = testBlock.getTransactions();
        for (int idx = 0; idx > txs.size(); idx++) {
            final Transaction tx = client.getTransactionByBlockNumberAndIndex(testBlock.getNumber(), idx);
            assertEquals(txs.get(idx).getHash(), tx.getHash());
        }
    }

    @Test
    public void testGetTransactionByHash() {
        testBlock.getTransactions().stream()
                .map(Transaction::getHash)
                .forEach(hash -> {
                    final Transaction tx = client.getTransactionByHash(hash);
                    assertEquals(hash, tx.getHash());
                });
    }

    @Test
    public void testGetTransactionReceipt() {
        assertNull(client.getTransactionReceipt(UNKNOWN_HASH));

        testBlock.getTransactions().forEach(tx -> {
            final TransactionReceipt receipt = client.getTransactionReceipt(tx.getHash());
            assertNotNull(receipt);
            assertEquals(testBlock.getNumber(), receipt.getBlockNumber());
            assertEquals(testBlock.getHash(), receipt.getBlockHash());
            assertEquals(tx.getHash(), receipt.getTransactionHash());
            assertEquals(tx.getTransactionIndex(), receipt.getTransactionIndex());
        });
    }

    @Test
    public void testGetTransactionsByAddress() {
        final int limit = 100;
        final List<Transaction> transactions = client.getTransactionsByAddress(testAccount.getAddress(), limit);
        assertTrue(transactions.size() > 0 && transactions.size() <= limit);
        transactions.forEach(tx -> {
            assertTrue(testAccount.getAddress().equals(tx.getFromAddress())
                    || testAccount.getAddress().equals(tx.getToAddress()));
        });
    }

    @Test
    public void testGetMempoolContent() {
        // Send a test transaction first
        final OutgoingTransaction outTx = createTransaction(testAccount.getAddress(), NULL_ADDRESS, 5_00000, MIN_FEE);
        final String txHash = client.sendTransaction(outTx);

        final List<Transaction> txsWithoutDetails = client.getMempoolContent(false);
        assertTrue(txsWithoutDetails.size() > 0);
        assertTrue(txsWithoutDetails.stream().anyMatch(tx -> tx.getHash().equals(txHash)));
        txsWithoutDetails.forEach(tx -> {
            assertNotNull(tx.getHash());
            assertNull(tx.getFrom());
            assertNull(tx.getTo());
            assertEquals(0, tx.getValue());
            assertNull(tx.getBlockHash());
            assertEquals(0, tx.getBlockNumber());
        });

        final List<Transaction> txsWithDetails = client.getMempoolContent(true);
        assertTrue(txsWithDetails.size() > 0);
        assertTrue(txsWithDetails.stream().anyMatch(tx -> tx.getHash().equals(txHash)));
        txsWithDetails.forEach(tx -> {
            assertNotNull(tx.getHash());
            assertNotNull(tx.getFrom());
            assertNotNull(tx.getTo());
            assertTrue(tx.getValue() > 0);
            assertNull(tx.getBlockHash());
            assertEquals(0, tx.getBlockNumber());
        });
    }

    @Test
    public void testGetMempool() {
        // Send several transactions with different fees
        final int[] fees = {100, 200, 500}; // per byte
        IntStream.of(fees).forEach(fee -> {
            client.sendTransaction(createTransaction(testAccount.getAddress(), NULL_ADDRESS, 3_00000, fee * MIN_FEE));
        });

        final Mempool mempool = client.getMempool();
        assertTrue(mempool.getTotal() >= fees.length);
        final int total = Arrays.stream(mempool.getBuckets()).map(mempool::getNumberOfTransactions).sum();
        assertEquals(total, mempool.getTotal());
        IntStream.of(fees).forEach(fee -> {
            assertTrue(mempool.getNumberOfTransactions(fee) > 0);
        });
    }

    @Test
    public void testGetSetMinFeePerByte() {
        final long oldValue = client.getMinFeePerByte();
        assertEquals(100, client.setMinFeePerByte(100));
        assertEquals(oldValue, client.setMinFeePerByte(oldValue));
    }

    // Miner

    @Test
    public void testIsMining() {
        assertFalse(client.isMining());
    }

    @Test
    public void testGetHashrate() {
        assertEquals(0, client.getHashrate());
    }

    @Test
    public void testGetMinerThreads() {
        assertTrue(client.getMinerThreads() > 0);
    }

    @Test
    public void testGetMinerAddress() {
        assertNotNull(client.getMinerAddress());
    }

    @Test
    public void testGetPoolAddress() {
        assertNull(client.getPoolAddress());
    }

    @Test
    public void testGetPoolConnectionState() {
        assertNotEquals(0, client.getPoolConnectionState());
    }

    @Test
    public void testGetPoolConfirmedBalance() {
        assertEquals(0, client.getPoolConfirmedBalance());
    }

    @Test
    public void testGetWork() {
        final Work work = client.getWork(testAccount.getAddress(), "");
        assertNotNull(work.getData());
        assertNotNull(work.getSuffix());
        assertTrue(work.getTarget() > 0);
        assertEquals("nimiq-argon2", work.getAlgorithm());
    }

    @Test
    public void testGetBlockTemplate() {
        final BlockTemplate template = client.getBlockTemplate(testAccount.getAddress(), "");
        assertNotNull(template.getHeader());
        assertTrue(template.getHeader().getHeight() > 0);
        assertNotNull(template.getInterlink());
        assertNotNull(template.getBody());
        assertNotNull(template.getBody().getHash());
        assertEquals(testAccount.getId(), template.getBody().getMinerAddr());
        assertTrue(template.getTarget() > 0);
        assertEquals(template.getTarget(), template.getHeader().getnBits());
    }

    @Test
    public void testSubmitBlock() {
        final Work work = client.getWork(testAccount.getAddress(), "");
        client.submitBlock(work.getData() + work.getSuffix());
    }

    // Accounts

    @Test
    public void testGetAccounts() {
        final List<Account> accounts = client.getAccounts();
        assertFalse(accounts.isEmpty());
        accounts.forEach(account -> {
            assertEquals(Account.Type.BASIC, account.getType());
        });
    }

    @Test
    public void testCreateAccount() {
        final Wallet wallet = client.createAccount();
        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertNotNull(wallet.getAddress());
        assertNotNull(wallet.getPublicKey());
        assertNull(wallet.getPrivateKey());

        // Try to find a new account by address
        assertTrue(client.getAccounts().stream().anyMatch(account -> account.getAddress().equals(wallet.getAddress())));
    }

    @Test
    public void testGetBalance() {
        assertTrue(client.getBalance(testAccount.getAddress()) > 0);
    }

    @Test
    public void testGetAccount() {
        final Account basicAccount = client.getAccount(testAccount.getAddress());
        assertEquals(Account.Type.BASIC, basicAccount.getType());
        assertTrue(basicAccount.getBalance() > 0);
    }

    // Blockchain

    @Test
    public void testGetBlockNumber() {
        assertTrue(client.getBlockNumber() > 0);
    }

    @Test
    public void testGetBlockTransactionCountByHash() {
        assertNull(client.getBlockTransactionCountByHash(UNKNOWN_HASH));
        assertEquals(Integer.valueOf(testBlock.getTransactions().size()),
                client.getBlockTransactionCountByHash(testBlock.getHash()));
    }

    @Test
    public void testGetBlockTransactionCountByNumber() {
        assertEquals(Integer.valueOf(testBlock.getTransactions().size()),
                client.getBlockTransactionCountByNumber(testBlock.getNumber()));
    }

    @Test
    public void testGetBlockByHash() {
        assertNull(client.getBlockByHash(UNKNOWN_HASH, false));

        final String blockHash = testBlock.getHash();

        final Block blockWithoutTx = client.getBlockByHash(blockHash, false);
        assertEquals(blockHash, blockWithoutTx.getHash());
        assertFalse(blockWithoutTx.getTransactions().isEmpty());
        blockWithoutTx.getTransactions().forEach(tx -> {
            assertNull(tx.getBlockHash());
        });

        final Block blockWithTxs = client.getBlockByHash(blockHash, true);
        assertEquals(blockHash, blockWithoutTx.getHash());
        assertFalse(blockWithTxs.getTransactions().isEmpty());
        blockWithTxs.getTransactions().forEach(tx -> {
            assertEquals(blockHash, tx.getBlockHash());
        });
    }

    @Test
    public void testGetBlockByNumber() {
        final int blockNumber = testBlock.getNumber();

        final Block blockWithoutTx = client.getBlockByNumber(blockNumber, false);
        assertEquals(blockNumber, blockWithoutTx.getNumber());
        assertFalse(blockWithoutTx.getTransactions().isEmpty());
        blockWithoutTx.getTransactions().forEach(tx -> {
            assertTrue(tx.getBlockNumber() == 0);
        });

        final Block blockWithTxs = client.getBlockByNumber(blockNumber, true);
        assertEquals(blockNumber, blockWithTxs.getNumber());
        assertFalse(blockWithTxs.getTransactions().isEmpty());
        blockWithTxs.getTransactions().forEach(tx -> {
            assertEquals(blockNumber, tx.getBlockNumber());
        });
    }

    // Misc

    @Test
    public void testGetSetConstant() {
        final long oldValue = client.getConstant("Mempool.SIZE_MAX"); // 50000
        assertEquals(100000, client.setConstant("Mempool.SIZE_MAX", 100000));
        assertEquals(oldValue, client.setConstant("Mempool.SIZE_MAX", "reset"));
    }

    @Test
    public void testSetLogLevel() {
        assertTrue(client.setLogLevel("*", "info"));
    }

    private static OutgoingTransaction createTransaction(final String from, final Account.Type fromType,
            final String to, final Account.Type toType,
            final long value, final long fee, String data, int flags) {
        final OutgoingTransaction outTx = new OutgoingTransaction();
        outTx.setFrom(from);
        outTx.setFromType(fromType);
        outTx.setTo(to);
        outTx.setToType(toType);
        outTx.setValue(value);
        outTx.setFee(fee);
        outTx.setData(data);
        outTx.setFlags(flags);
        return outTx;
    }

    private static OutgoingTransaction createTransaction(final String from, final String to,
            final long value, final long fee) {
        return createTransaction(from, Account.Type.BASIC, to, Account.Type.BASIC, value, fee, null, 0);
    }
}
