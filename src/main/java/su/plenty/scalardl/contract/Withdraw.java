package su.plenty.scalardl.contract;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Withdraw extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
	return Json.createObjectBuilder().build();
  }
}
