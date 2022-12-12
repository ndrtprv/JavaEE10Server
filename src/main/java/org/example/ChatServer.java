package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Runnable{
    private final Map<Integer, Socket> mapClient = new HashMap<>();

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8887)) {
            System.out.println("Server was enabled. Waiting for new client...");

            int numClient = 1;
            Socket client;

            while (true) {
                client = serverSocket.accept();
                Thread clientThread = new Thread(new ClientThread
                        (client, this, numClient));
                clientThread.setDaemon(true);
                clientThread.start();
                mapClient.put(numClient, client);
                numClient++;
            }
        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println(e.getMessage());
        }
    }

    public void clientExitedChat(int n) {
        mapClient.remove(n);
    }

    public void sendMessageForAllClient(int numClient, String clientMessage) {
        for (int i: mapClient.keySet()) {
            if (i != numClient) {
                System.out.println("Sending message to client " + i + "...");

                BufferedWriter outputUser;

                try {
                    outputUser = new BufferedWriter(new OutputStreamWriter(mapClient.get(i).getOutputStream()));
                    outputUser.write("Client " + numClient + ": "  + clientMessage);
                    outputUser.flush();
                } catch (IOException e) {
                    System.out.println("ERROR!");
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}