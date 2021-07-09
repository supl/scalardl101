package su.plenty.scalardl.contract;

import com.scalar.dl.ledger.contract.Contract;
import com.scalar.dl.ledger.database.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Ok extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    if (argument.containsKey("id")) {
      String id = argument.getString("id");

      JsonObject value = Json.createObjectBuilder().add("state", "ok")
          .add("timestamp", System.currentTimeMillis() / 1000).build();

      ledger.get(id);
      ledger.put(id, value);
    }

    return null;
  }
}
