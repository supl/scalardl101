package su.plenty.scalardl.contract;

import com.scalar.dl.ledger.contract.Contract;
import com.scalar.dl.ledger.database.AssetFilter;
import com.scalar.dl.ledger.database.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class History extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    if (argument.containsKey("id")) {
      String id = argument.getString("id");

      AssetFilter filter = new AssetFilter(id);
      JsonArrayBuilder history = Json.createArrayBuilder();

      ledger
          .scan(filter)
          .forEach(
              asset -> {
                history.add(asset.data());
              });

      return Json.createObjectBuilder().add("history", history.build()).build();
    }

    return null;
  }
}
