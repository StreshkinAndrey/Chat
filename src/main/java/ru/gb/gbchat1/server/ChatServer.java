package ru.gb.gbchat1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import ru.gb.gbchat1.Command;

public class ChatServer {

    private static final Logger logger = LogManager.getLogger(ChatServer.class);

    private final Map<String, ClientHandler> clients;

    public ChatServer() {
        this.clients = new HashMap<>();
    }

    public void run() {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(8189);
             AuthService authService = new InMemoryAuthService()) {
            while (true) {
                logger.debug("Сервер запускается...");
                logger.info("Wait client connection...");
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this, authService, executorService);
                logger.info("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            executorService.shutdownNow();
        }
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(), client);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick());
        broadcastClientList();
    }

    private void broadcastClientList() {
        StringBuilder nicks = new StringBuilder();
        for (ClientHandler value : clients.values()) {
            nicks.append(value.getNick()).append(" ");
        }
//        final String nicks = clients.values().stream()
//                .map(ClientHandler::getNick)
//                .collect(Collectors.joining(" "));
        broadcast(Command.CLIENTS, nicks.toString().trim());
    }

    private void broadcast(Command command, String nicks) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, nicks);
        }
    }

    public void broadcast(String msg) {
        clients.values().forEach(client -> client.sendMessage(msg));
    }

    public void sendMessageToClient(ClientHandler sender, String to, String message) {
        final ClientHandler receiver = clients.get(to);
        if (receiver != null) {
            receiver.sendMessage("от " + sender.getNick() + ": " + message);
            sender.sendMessage("участнику " + to + ": " + message);
        } else {
            sender.sendMessage(Command.ERROR, "Участника с ником " + to + " нет в чате!");
            logger.debug("Участника с ником " + to + " нет в чате!");
        }
    }
}