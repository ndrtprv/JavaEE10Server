package org.example;

public class Main {
    public static void main(String[] args) {

        ChatServer chatServer = new ChatServer();
        Thread thread = new Thread(chatServer);
        thread.start();

    }
}