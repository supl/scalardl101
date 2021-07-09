# Introduction
This CLI is used in the crab supply chain.
A crab denoted by `id` will be checked by fishers, cooperatives, sellers, ...etc.
The checked results can be committed into the ledger.
All check history of a crab can be traced.

# Setup
This CLI needs a Scalar DL network to work. Start a local Scalar DL network by running

```
docker-compose up -d
```

# Build
To build the CLI you need:
- JDK for Java 8
- Gradle 6

Then, do
```
make
```

The executables will be outputed in `build/install/scalardl101/bin/scalardl101`

# Use
### Check "OK" for a crab

```
build/install/scalardl101/bin/scalardl101 ok <id>
```

### Check "Not Good" for a crab
```
build/install/scalardl101/bin/scalardl101 notgood <id>
```

### Trace all checks of a crab
```
build/install/scalardl101/bin/scalardl101 history <id>
```
