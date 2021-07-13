package su.plenty.scalardl.contract;

import com.scalar.dl.ledger.contract.Contract;
import com.scalar.dl.ledger.database.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class NotGood extends Contract {

  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    if (argument.containsKey("id") && argument.containsKey("timestamp")) {
      String id = argument.getString("id");
      long timestamp = (long) argument.getInt("timestamp");

      JsonObject value =
          Json.createObjectBuilder().add("state", "notgood").add("timestamp", timestamp).build();

      ledger.get(id);
      ledger.put(id, value);
    }

    return null;
  }
}
