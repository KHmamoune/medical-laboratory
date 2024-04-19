package com.example.laboratory;

import java.io.IOException;
import java.net.Socket;

public class ClientManager {
    private static Client sharedClient;

    // Initialize the shared client
    public static void initializeClient() {
        try {
            sharedClient = new Client(new Socket("localhost", 1234));
            System.out.println("Connected to Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get the shared client
    public static Client getSharedClient() {
        return sharedClient;
    }
}
