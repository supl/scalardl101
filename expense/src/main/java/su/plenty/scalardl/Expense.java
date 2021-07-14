package su.plenty.scalardl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.dl.client.config.ClientConfig;
import com.scalar.dl.client.service.ClientModule;
import com.scalar.dl.client.service.ClientService;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Expense {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      return;
    }

    ClientConfig config = new ClientConfig(new FileInputStream("client.properties"));
    Injector injector = Guice.createInjector(new ClientModule(config));
    ClientService service = injector.getInstance(ClientService.class);

    try {
      service.registerCertificate();
    } catch (Exception e) {
    }

    try {
      service.registerContract(
          "expense", "su.plenty.scalardl.contract.Expense", "Expense.class", Optional.empty());
    } catch (Exception e) {
    }

    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
    JsonObject argument =
        Json.createObjectBuilder()
            .add("date", date)
            .add("expense", Integer.parseInt(args[0]))
            .add("log", args[1])
            .build();

    service.executeContract("expense", argument);
  }
}
