package pt.tecnico.grpc.server;

import pt.tecnico.grpc.Banking.RegisterResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Bank {
    private static final Logger LOGGER = Logger.getLogger(Bank.class.getName());
    ConcurrentHashMap<String, Integer> clients = new ConcurrentHashMap<>();

    public void register(String client, Integer balance) {
        clients.put(client, balance);
        LOGGER.info("User: " + client + " has been instantiated with balance: " + balance);
    }
    public int getBalance(String client) {
        Integer balance = clients.get(client);
        return balance;

    }
    public boolean isClient(String client) {
        return (clients.get(client) != null);
    }
    public boolean getSubsidize(Integer threshold, Integer amount) {
        for (String key : clients.keySet()) {
            if(clients.get(key) > threshold)
                return false;
        }
        for (String key : clients.keySet()) {
            clients.replace(key, amount+clients.get(key));
        }
        return true;
    }
}
