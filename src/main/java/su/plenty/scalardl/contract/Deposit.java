package su.plenty.scalardl.contract;

import com.scalar.ledger.asset.Asset;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.exception.ContractContextException;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Deposit extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
    if (!argument.containsKey("account") || !argument.containsKey("amount")) {
      throw new ContractContextException("invalid argument");
    }

    String account = argument.getString("account");
    int amount = argument.getInt("amount");

    Optional<Asset> asset =  ledger.get(account);
    int balance = 0;
    if (asset.isPresent()) {
      balance = asset.get().data().getInt("balance");
    }

    balance += amount;

    JsonObject value = Json.createObjectBuilder().add("balance", balance).build();
    ledger.put(account, value);

    return value;
  }
}

