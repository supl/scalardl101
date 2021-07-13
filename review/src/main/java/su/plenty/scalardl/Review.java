package su.plenty.scalardl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.dl.client.config.ClientConfig;
import com.scalar.dl.client.service.ClientModule;
import com.scalar.dl.client.service.ClientService;
import com.scalar.dl.ledger.model.ContractExecutionResult;
import java.io.FileInputStream;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class Review {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
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
			service.registerContract("review", "su.plenty.scalardl.contract.Review", "Review.class",
					Optional.empty());
		} catch (Exception e) {
		}

		try {
			JsonObject argument = Json.createObjectBuilder().add("date", args[0]).build();
			ContractExecutionResult result = service.executeContract("review", argument);

			if (result.getResult().isPresent()) {
				JsonArray expenses = result.getResult().get().getJsonArray("expenses");
				expenses.forEach(expense -> {
					System.out.printf("%4d %s\n", expense.asJsonObject().getInt("expense"),
							expense.asJsonObject().getString("log"));
				});

				System.out.println("------------------------------------------");
				System.out.printf("%4d\n", result.getResult().get().getInt("total"));
			}
		} catch (Exception e) {
			System.out.printf("No expenses data for %s\n", args[0]);
		}
	}
}
