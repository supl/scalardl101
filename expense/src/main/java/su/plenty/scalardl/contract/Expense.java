package su.plenty.scalardl.contract;

import com.scalar.dl.ledger.asset.Asset;
import com.scalar.dl.ledger.contract.Contract;
import com.scalar.dl.ledger.database.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class Expense extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    if (argument.containsKey("date") && argument.containsKey("expense")
        && argument.containsKey("log")) {
      String date = argument.getString("date");
      String log = argument.getString("log");
      int expense = argument.getInt("expense");

      JsonObject newExpense =
          Json.createObjectBuilder().add("expense", expense).add("log", log).build();

      JsonObject value;
      Optional<Asset> asset = ledger.get(date);
      if (asset.isPresent()) {
        value = asset.get().data();
        int newTotal = value.getInt("total") + expense;
        JsonArray expenses =
            Json.createArrayBuilder(value.getJsonArray("expenses")).add(newExpense).build();
        value = Json.createObjectBuilder(value).add("expenses", expenses).add("total", newTotal)
            .build();
      } else {
        JsonArray expenses = Json.createArrayBuilder().add(newExpense).build();
        value = Json.createObjectBuilder().add("expenses", expenses).add("total", expense).build();
      }

      ledger.put(date, value);
    }

    return null;
  }
}
