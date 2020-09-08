# Nimiq Java Client

> Java implementation of the Nimiq RPC client specs.

## Usage

Here's a simple example that reports the info about the current head block and its miner account:
```java
NimiqClientFactory clientFactory = new NimiqClientFactory(new URL("http://localhost:8648/"));
NimiqClient client = clientFactory.getClient();

// Wait for consensus to establish
ConsensusState consensusState;
while ((consensusState = client.getConsensusState()) != ConsensusState.ESTABLISHED) {
    if (consensusState == ConsensusState.CONNECTING) {
        System.out.println("Connecting to the network");
    } else if (consensusState == ConsensusState.SYNCING) {
        SyncingState syncingState = client.getSyncingState();
        System.out.printf("Syncing at block #%d\n", syncingState.getCurrentBlock());
    }

    Thread.sleep(1000);
}
System.out.println("Consensus established");

// Display the head block
int blockNumber = client.getBlockNumber();
Block block = client.getBlockByNumber(blockNumber, false);
long balance = client.getBalance(block.getMinerAddress());

System.out.printf("Head block #%d, %s mined by %s who has %.5f NIM.\n", block.getNumber(), block.getHash(),
    block.getMinerAddress(), NimiqUtils.lunasToCoins(balance));
```

## API

See [/docs](/docs) or [GitHub Pages](https://nimiq-community.github.io/java-client/).

You can also refer to the [original specs](https://github.com/nimiq/core-js/wiki/JSON-RPC-API).

## Installation

Add the dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>com.nimiq</groupId>
    <artifactId>nimiq-rpc-client</artifactId>
    <version>1.0</version>
</dependency>
```

## Build

```
mvn -DskipTests source:jar javadoc:jar install
```

## Test

You need a test Nimiq node started like this:

```
nodejs index.js --protocol=dumb --type=full --network=test --rpc
```

There must be a wallet funded with a few hundred NIM. To run the test:

```
mvn test
```

## Contributions

This implementation was originally contributed by [Mat (a.k.a. Tomkha)](https://github.com/tomkha/).

Please send your contributions as pull requests.
Refer to the [issue tracker](https://github.com/nimiq-community/java-client/issues) for ideas.

## License

[Apache 2.0](LICENSE)
