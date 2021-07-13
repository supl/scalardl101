package su.plenty.scalardl.contract;

import com.scalar.dl.ledger.asset.Asset;
import com.scalar.dl.ledger.contract.Contract;
import com.scalar.dl.ledger.database.Ledger;
import java.util.Optional;
import javax.json.JsonObject;

public class Review extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    if (argument.containsKey("date")) {
      String date = argument.getString("date");

      Optional<Asset> asset = ledger.get(date);
      if (asset.isPresent()) {
        return asset.get().data();
      }
    }

    return null;
  }
}
