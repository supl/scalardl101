package su.plenty.scalardl;

import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.client.config.ClientConfig;
import com.scalar.client.service.ClientModule;
import com.scalar.client.service.ClientService;
import com.scalar.client.service.StatusCode;
import com.scalar.rpc.ledger.ContractExecutionResponse;
import com.scalar.rpc.ledger.LedgerServiceResponse;

class Main {
    public static void main(String[] args) {
        try {
            ClientConfig config = new ClientConfig(new FileInputStream("client.properties"));
            Injector injector = Guice.createInjector(new ClientModule(config));
            try (ClientService clientService = injector.getInstance(ClientService.class)) {
                registerCertificate(clientService);
                registerContract(clientService);

                executeContractDeposit(clientService, "plenty", 100);
                executeContractDeposit(clientService, "plenty", 200);
                executeContractWithdraw(clientService, "plenty", 50);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return;
    }

    private static void registerCertificate(ClientService clientService) throws Exception {
        LedgerServiceResponse response = clientService.registerCertificate();
        if (response.getStatus() == StatusCode.CERTIFICATE_ALREADY_REGISTERED.get()) {
            return;
        }

        if (response.getStatus() != StatusCode.OK.get()) {
            throw new Exception(response.getStatus() + " " + response.getMessage());
        }
    }

    private static void registerContract(ClientService clientService) throws Exception {
        LedgerServiceResponse response;
        response = clientService.registerContract("withdraw", "su.plenty.scalardl.contract.Withdraw", "Withdraw.class", Optional.empty());
        if ((response.getStatus() != StatusCode.OK.get()) && (response.getStatus() != StatusCode.CONTRACT_ALREADY_REGISTERED.get())) {
            throw new Exception(response.getStatus() + " " + response.getMessage());
        }
        System.out.println("contract withdraw registered");

        response = clientService.registerContract("deposit", "su.plenty.scalardl.contract.Deposit", "Deposit.class", Optional.empty());
        if ((response.getStatus() != StatusCode.OK.get()) && (response.getStatus() != StatusCode.CONTRACT_ALREADY_REGISTERED.get())) {
            throw new Exception(response.getStatus() + " " + response.getMessage());
        }
        System.out.println("contract deposit registered");
    }

    private static void executeContractDeposit(ClientService clientService, String account, int amount) throws Exception {
        JsonObject argument = Json.createObjectBuilder().add("account", account).add("amount", amount).build();
        ContractExecutionResponse response = clientService.executeContract("deposit", argument);
        if (response.getStatus() != StatusCode.OK.get()) {
            throw new Exception(response.getStatus() + " " + response.getMessage());
        }
        System.out.println("deposited " + amount + " to " + account);

        JsonReader reader = Json.createReader(new StringReader(response.getResult()));
        int balance = reader.readObject().getInt("balance");
        reader.close();
        System.out.println("balance = " + balance);
    }

    private static void executeContractWithdraw(ClientService clientService, String account, int amount) throws Exception {
        JsonObject argument = Json.createObjectBuilder().add("account", account).add("amount", amount).build();
        ContractExecutionResponse response = clientService.executeContract("withdraw", argument);
        if (response.getStatus() != StatusCode.OK.get()) {
            throw new Exception(response.getStatus() + " " + response.getMessage());
        }
        System.out.println("withdrawed " + amount + " from " + account);

        JsonReader reader = Json.createReader(new StringReader(response.getResult()));
        int balance = reader.readObject().getInt("balance");
        reader.close();
        System.out.println("balance = " + balance);
    }
}
