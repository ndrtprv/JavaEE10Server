package org.example;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable{
    private final Socket clientSocket;
    private final ChatServer chatServer;
    private final int numClient;

    public ClientThread(Socket clientSocket,
                        ChatServer chatServer, int numClient) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        this.numClient = numClient;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client " + numClient + " has been connected.");

            new PrintWriter(clientSocket.getOutputStream(), true).println("Client " + numClient);

            String clientMessage;
            while (true) {
                clientMessage = in.readLine();
                if (!"exit".equalsIgnoreCase(clientMessage)) {
                    System.out.println("Client " + numClient + " has sent message: " + clientMessage);
                    chatServer.sendMessageForAllClient(numClient, clientMessage);
                } else {
                    System.out.println("Client " + numClient + " has been disconnected.");
                    chatServer.clientExitedChat(numClient);
                    in.close();
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}