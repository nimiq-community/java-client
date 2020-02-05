
package com.nimiq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

    private static final String GENESIS_BLOCK = "0001000000000000000000000000000000000000000000"
            + "00000000000000000000000000000000000000000000000000000000000000000000000000000000"
            + "000000f6ba2bbf7e1478a209057000471d73fbdc28df0b717747d929cfde829c4120f62e02da3d16"
            + "2e20fa982029dbde9cc20f6b431ab05df1764f34af4c62a4f2b33f1f010000000000015ac3185f00"
            + "0134990001000000000000000000000000000000000000000007546573744e657400000000";
    private static final String BURN_ADDRESS = "NQ07 0000 0000 0000 0000 0000 0000 0000 0000";
    private static final String FAUCET_ADDRESS = "NQ02 YP68 BA76 0KR3 QY9C SF0K LP8Q THB6 LTKU";
    private static final String VESTING_ACCOUNT_ADDRESS = "NQ74 919Y 3HK2 XFKP MM7C YF2S EUG3 PTV2 S2PF";
    private static final String HTLC_ADDRESS = "NQ26 4XVR N8BB THR4 RB81 MF3V C5DM 49V5 7TQU";
    private static final int BLOCK_NUMBER = 36360;
    private static final String BLOCK_HASH = "2c9d91f8d7061f2386e4221370ed9dec53b96c9d42026f20bd8853ed74f2b2ef";
    private static final String TX1_HASH = "69834d7b39e05a3b077124dfebde54d0ced636bc98c46ea68fbf885e34a0fbeb";
    private static final String TX2_HASH = "eb4b690aa5daf0c416f5cd7a7b8f9d4100a865b858a23de7d99b9dcfe58086cd";
    private static final String UNKNOWN_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    private static final int TX_SIZE = 138;
    private static final long MIN_FEE = TX_SIZE; // 1 luna/byte

    private static NimiqClient client;

    @BeforeClass
    public static void createClient() throws MalformedURLException {
        final URL url = new URL(RPC_URL);
        client = new NimiqClientFactory(url).getClient();
    }

    // Network

    @Test
    public void testPeerCount() {
        assertTrue(client.peerCount() > 0);
    }

    @Test
    public void testSyncing() {
        final SyncStatus status = client.syncing();
        if (status.isSyncing()) {
            assertTrue(status.getHighestBlock() > 0);
        }
    }

    @Test
    public void testConsensus() {
        assertNotNull(client.consensus());
    }

    @Test
    public void testPeerList() {
        final List<PeerInfo> peers = client.peerList();
        assertFalse(peers.isEmpty());
        peers.forEach(peer -> {
            assertNotNull(peer.getId());
            assertNotNull(peer.getAddress());
            if (peer.getAddressState() == PeerInfo.AddressState.ESTABLISHED) {
                assertEquals(PeerInfo.ConnectionState.ESTABLISHED, peer.getConnectionState());
                assertNotNull(peer.getHeadHash());
            }
        });
    }

    @Test
    public void testPeerState() {
        client.peerList().forEach(peer -> {
            final PeerInfo state = client.peerState(peer.getAddress());
            assertEquals(peer.getId(), state.getId());
            assertEquals(peer.getAddress(), state.getAddress());
            assertEquals(peer.getAddressState(), state.getAddressState());
        });

        // try to connect to all new nodes
        client.peerList().stream().filter(peer -> peer.getAddressState() == PeerInfo.AddressState.NEW).forEach(peer -> {
            final PeerInfo state = client.peerState(peer.getAddress(), "connect");
            assertNotNull(state.getConnectionState());
        });
    }

    // Transactions

    private String getSenderAddress(final long minValue) {
        return client.accounts().stream().filter(account -> account.getBalance() >= minValue).findFirst()
                .map(Account::getAddress)
                .orElseThrow(() -> new UnsupportedOperationException("Can't find account with enough balance"));
    }

    private OutgoingTransaction createTransaction(final String from, final String to, final long value,
            final long fee) {
        final OutgoingTransaction outTx = new OutgoingTransaction();
        outTx.setFrom(from);
        outTx.setTo(to);
        outTx.setValue(value);
        outTx.setFee(fee);
        return outTx;
    }

    @Test
    public void testSendRawTransaction() {
        final String sender = getSenderAddress(100_00000);
        final OutgoingTransaction outTx = createTransaction(sender, FAUCET_ADDRESS, 13_00000, MIN_FEE);

        String txHash = client.sendRawTransaction(client.createRawTransaction(outTx));
        assertNotNull(txHash);
        assertNotNull(client.getTransactionByHash(txHash));
    }

    @Test
    public void testCreateRawTransaction() {
        final String sender = getSenderAddress(100_00000); // No real spending
        final OutgoingTransaction outTx = createTransaction(sender, FAUCET_ADDRESS, 12_00000, MIN_FEE);

        final String txHex = client.createRawTransaction(outTx);
        assertNotNull(txHex);
        assertEquals(TX_SIZE, txHex.length() / 2);
    }

    @Test
    public void testSendTransaction() {
        final String sender = getSenderAddress(100_00000);
        final OutgoingTransaction outTx = createTransaction(sender, FAUCET_ADDRESS, 10_00000, MIN_FEE);

        String txHash = client.sendTransaction(outTx);
        assertNotNull(txHash);
        assertNotNull(client.getTransactionByHash(txHash));
    }

    @Test
    public void testGetRawTransactionInfo() {
        final String sender = getSenderAddress(100_00000);
        final OutgoingTransaction outTx = createTransaction(sender, FAUCET_ADDRESS, 8_00000, MIN_FEE);
        final String txHex = client.createRawTransaction(outTx);

        final Transaction txBefore = client.getRawTransactionInfo(txHex);
        assertNotNull(txBefore.getHash());
        assertEquals(sender, txBefore.getFromAddress());
        assertEquals(FAUCET_ADDRESS, txBefore.getToAddress());
        assertEquals(8_00000, txBefore.getValue());
        assertEquals(MIN_FEE, txBefore.getFee());
        assertTrue(txBefore.isValid());
        assertFalse(txBefore.isInMempool());

        String txHash = client.sendRawTransaction(txHex);
        final Transaction txAfter = client.getRawTransactionInfo(txHex);
        assertEquals(txHash, txAfter.getHash());
        assertTrue(txAfter.isValid());
        assertTrue(txAfter.isInMempool());
    }

    @Test
    public void testGetTransactionByBlockHashAndIndex() {
        assertNull(client.getTransactionByBlockHashAndIndex(UNKNOWN_HASH, 0));
        assertNull(client.getTransactionByBlockHashAndIndex(BLOCK_HASH, 999));
        assertEquals(TX1_HASH, client.getTransactionByBlockHashAndIndex(BLOCK_HASH, 0).getHash());
        assertEquals(TX2_HASH, client.getTransactionByBlockHashAndIndex(BLOCK_HASH, 1).getHash());
    }

    @Test
    public void testGetTransactionByBlockNumberAndIndex() {
        assertNull(client.getTransactionByBlockNumberAndIndex(BLOCK_NUMBER, 999));
        assertEquals(TX1_HASH, client.getTransactionByBlockNumberAndIndex(BLOCK_NUMBER, 0).getHash());
        assertEquals(TX2_HASH, client.getTransactionByBlockNumberAndIndex(BLOCK_NUMBER, 1).getHash());
    }

    @Test
    public void testGetTransactionByHash() {
        assertEquals(TX1_HASH, client.getTransactionByHash(TX1_HASH).getHash());
        assertEquals(TX2_HASH, client.getTransactionByHash(TX2_HASH).getHash());
    }

    @Test
    public void testGetTransactionReceipt() {
        assertNull(client.getTransactionReceipt(UNKNOWN_HASH));

        final TransactionReceipt receipt = client.getTransactionReceipt(TX1_HASH);
        assertNotNull(receipt);
        assertEquals(BLOCK_NUMBER, receipt.getBlockNumber());
        assertEquals(BLOCK_HASH, receipt.getBlockHash());
        assertEquals(TX1_HASH, receipt.getTransactionHash());
        assertEquals(0, receipt.getTransactionIndex());
    }

    @Test
    public void testGetTransactionsByAddress() {
        final int limit = 100;
        final List<Transaction> transactions = client.getTransactionsByAddress(FAUCET_ADDRESS, limit);
        assertEquals(limit, transactions.size());
        transactions.forEach(tx -> {
            assertTrue(FAUCET_ADDRESS.equals(tx.getFromAddress()) || FAUCET_ADDRESS.equals(tx.getToAddress()));
        });
    }

    @Test
    public void testMempoolContent() {
        final String sender = getSenderAddress(100_00000);

        // Send a test transaction first
        final OutgoingTransaction outTx = createTransaction(sender, FAUCET_ADDRESS, 5_00000, MIN_FEE);
        final String txHash = client.sendTransaction(outTx);

        final List<Transaction> txsWithoutDetails = client.mempoolContent(false);
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

        final List<Transaction> txsWithDetails = client.mempoolContent(true);
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
    public void testMempool() {
        final String sender = getSenderAddress(100_00000);

        // Send several transactions with different fees
        int[] fees = { 100, 200, 500 }; // per byte
        IntStream.of(fees).forEach(fee -> {
            client.sendTransaction(createTransaction(sender, FAUCET_ADDRESS, 3_00000, fee * MIN_FEE));
        });

        final Mempool mempool = client.mempool();
        assertTrue(mempool.getTotal() >= fees.length);
        final int total = Arrays.stream(mempool.getBuckets()).map(mempool::getNumberOfTransactions).sum();
        assertEquals(total, mempool.getTotal());
        IntStream.of(fees).forEach(fee -> {
            assertTrue(mempool.getNumberOfTransactions(fee) > 0);
        });
    }

    @Test
    public void testMinFeePerByte() {
        final long oldValue = client.minFeePerByte();
        assertEquals(100, client.minFeePerByte(100));
        assertEquals(oldValue, client.minFeePerByte(oldValue));
    }

    // Miner

    @Test
    public void testMining() {
        assertFalse(client.mining());
    }

    @Test
    public void testHashrate() {
        assertEquals(0, client.hashrate());
    }

    @Test
    public void testMinerThreads() {
        assertTrue(client.minerThreads() > 0);
    }

    @Test
    public void testMinerAddress() {
        assertNotNull(client.minerAddress());
    }

    @Test
    public void testPool() {
        assertNull(client.pool());
    }

    @Test
    public void testPoolConnectionState() {
        assertNotEquals(0, client.poolConnectionState());
    }

    @Test
    public void testPoolConfirmedBalance() {
        assertEquals(0, client.poolConfirmedBalance());
    }

    @Test
    public void testGetWork() {
        final Work work = client.getWork(BURN_ADDRESS, "");
        assertNotNull(work.getData());
        assertNotNull(work.getSuffix());
        assertTrue(work.getTarget() > 0);
        assertEquals("nimiq-argon2", work.getAlgorithm());
    }

    @Test
    public void testGetBlockTemplate() {
        final BlockTemplate template = client.getBlockTemplate(BURN_ADDRESS, "");
        assertNotNull(template.getHeader());
        assertTrue(template.getHeader().getHeight() > 0);
        assertNotNull(template.getInterlink());
        assertNotNull(template.getBody());
        assertNotNull(template.getBody().getHash());
        assertEquals("0000000000000000000000000000000000000000", template.getBody().getMinerAddr());
        assertTrue(template.getTarget() > 0);
        assertEquals(template.getTarget(), template.getHeader().getnBits());
    }

    @Test
    public void testSubmitBlock() {
        client.submitBlock(GENESIS_BLOCK);
    }

    // Accounts

    @Test
    public void testAccounts() {
        final List<Account> accounts = client.accounts();
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
        assertTrue(client.accounts().stream().anyMatch(account -> account.getAddress().equals(wallet.getAddress())));
    }

    @Test
    public void testGetBalance() {
        assertTrue(client.getBalance(BURN_ADDRESS) > 0);
    }

    @Test
    public void testGetAccount() {
        final Account basicAccount = client.getAccount(BURN_ADDRESS);
        assertEquals(Account.Type.BASIC, basicAccount.getType());
        assertTrue(basicAccount.getBalance() > 0);

        final Account vestingAccount = client.getAccount(VESTING_ACCOUNT_ADDRESS);
        assertEquals(Account.Type.VESTING, vestingAccount.getType());
        assertTrue(vestingAccount.getBalance() > 0);
        assertNotNull(vestingAccount.getOwnerAddress());
        assertTrue(vestingAccount.getVestingStart() > 0);
        assertTrue(vestingAccount.getVestingStepBlocks() > 0);
        assertTrue(vestingAccount.getVestingStepAmount() > 0);
        assertTrue(vestingAccount.getVestingTotalAmount() > 0);

        final Account htlcAccount = client.getAccount(HTLC_ADDRESS);
        assertEquals(Account.Type.HTLC, htlcAccount.getType());
        assertTrue(htlcAccount.getBalance() > 0);
        assertNotNull(htlcAccount.getSenderAddress());
        assertNotNull(htlcAccount.getRecipientAddress());
        assertNotNull(htlcAccount.getHashRoot());
        assertTrue(htlcAccount.getHashCount() > 0);
        assertTrue(htlcAccount.getTotalAmount() > 0);
    }

    // Blockchain

    @Test
    public void testBlockNumber() {
        assertTrue(client.blockNumber() > 0);
    }

    @Test
    public void testGetBlockTransactionCountByHash() {
        assertNull(client.getBlockTransactionCountByHash(UNKNOWN_HASH));
        assertEquals(Integer.valueOf(2), client.getBlockTransactionCountByHash(BLOCK_HASH));
    }

    @Test
    public void testGetBlockTransactionCountByNumber() {
        assertEquals(Integer.valueOf(2), client.getBlockTransactionCountByNumber(BLOCK_NUMBER));
    }

    @Test
    public void testGetBlockByHash() {
        assertNull(client.getBlockByHash(UNKNOWN_HASH, false));

        final Block blockWithoutTx = client.getBlockByHash(BLOCK_HASH, false);
        assertEquals(BLOCK_HASH, blockWithoutTx.getHash());
        assertFalse(blockWithoutTx.getTransactions().isEmpty());
        blockWithoutTx.getTransactions().forEach(tx -> {
            assertNull(tx.getBlockHash());
        });

        final Block blockWithTxs = client.getBlockByHash(BLOCK_HASH, true);
        assertEquals(BLOCK_HASH, blockWithoutTx.getHash());
        assertFalse(blockWithTxs.getTransactions().isEmpty());
        blockWithTxs.getTransactions().forEach(tx -> {
            assertEquals(BLOCK_HASH, tx.getBlockHash());
        });
    }

    @Test
    public void testGetBlockByNumber() {
        final Block blockWithoutTx = client.getBlockByNumber(BLOCK_NUMBER, false);
        assertEquals(BLOCK_NUMBER, blockWithoutTx.getNumber());
        assertFalse(blockWithoutTx.getTransactions().isEmpty());
        blockWithoutTx.getTransactions().forEach(tx -> {
            assertTrue(tx.getBlockNumber() == 0);
        });

        final Block blockWithTxs = client.getBlockByNumber(BLOCK_NUMBER, true);
        assertEquals(BLOCK_NUMBER, blockWithTxs.getNumber());
        assertFalse(blockWithTxs.getTransactions().isEmpty());
        blockWithTxs.getTransactions().forEach(tx -> {
            assertEquals(BLOCK_NUMBER, tx.getBlockNumber());
        });
    }

    // Misc

    @Test
    public void testConstant() {
        final long oldValue = client.constant("Mempool.SIZE_MAX"); // 50000
        assertEquals(100000, client.constant("Mempool.SIZE_MAX", 100000));
        assertEquals(oldValue, client.constant("Mempool.SIZE_MAX", "reset"));
    }

    @Test
    public void testLog() {
        assertTrue(client.log("*", "info"));
    }

}
