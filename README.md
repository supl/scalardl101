# Introduction
This CLI is used to record my daily expenses.

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

Two executables will be outputed in
- `./expense/build/install/expense/bin/expense`
- `./review/build/install/review/bin/review`

# Use
### Log an expense

```
./expense/build/install/scalardl101/bin/expense <amount> <log>
```

### Check some day's expenses

```
./review/build/install/scalardl101/bin/review <yyyymmdd>
```
