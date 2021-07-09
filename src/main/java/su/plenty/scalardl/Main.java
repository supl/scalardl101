package su.plenty.scalardl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.dl.client.config.ClientConfig;
import com.scalar.dl.client.service.ClientModule;
import com.scalar.dl.client.service.ClientService;
import com.scalar.dl.ledger.model.ContractExecutionResult;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

class Main {
  static ClientService service;

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      return;
    }

    ClientConfig config = new ClientConfig(new FileInputStream("client.properties"));
    Injector injector = Guice.createInjector(new ClientModule(config));
    service = injector.getInstance(ClientService.class);

    try {
      service.registerCertificate();
    } catch (Exception e) {
    }

    switch (args[0]) {
      case "ok":
        ok(args[1]);
        break;
      case "notgood":
        notgood(args[1]);
        break;

      case "history":
        history(args[1]);
        break;
    }

    return;
  }

  private static void ok(String id) {
    try {
      service.registerContract(
          "ok", "su.plenty.scalardl.contract.Ok", "Ok.class", Optional.empty());
    } catch (Exception e) {
    }

    JsonObject argument = Json.createObjectBuilder().add("id", id).build();
    service.executeContract("ok", argument);
  }

  private static void notgood(String id) {
    try {
      service.registerContract(
          "notgood", "su.plenty.scalardl.contract.NotGood", "NotGood.class", Optional.empty());
    } catch (Exception e) {
    }

    JsonObject argument = Json.createObjectBuilder().add("id", id).build();
    service.executeContract("notgood", argument);
  }

  private static void history(String id) {
    try {
      service.registerContract(
          "history", "su.plenty.scalardl.contract.History", "History.class", Optional.empty());
    } catch (Exception e) {
    }

    JsonObject argument = Json.createObjectBuilder().add("id", id).build();
    ContractExecutionResult result = service.executeContract("history", argument);

    if (!result.getResult().isPresent()) {
      return;
    }

    JsonArray history = result.getResult().get().getJsonArray("history");
    System.out.println("Check History");

    history.forEach(
        h -> {
          long timestamp = (long) h.asJsonObject().getInt("timestamp");
          String state = h.asJsonObject().getString("state");
          String datetime =
              new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000));

          System.out.println(datetime + ": " + state);
        });
  }
}
