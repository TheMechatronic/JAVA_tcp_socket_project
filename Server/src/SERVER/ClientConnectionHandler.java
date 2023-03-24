package SERVER;

import json_toolkit.User;
import server_toolkit.sResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnectionHandler extends Thread {
    protected Socket socket;
    public static boolean shouldClose = false;
    public static User userLoggedIn = null;
    public ClientConnectionHandler(Socket socket) {
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        try {
            handleClientMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClientMessages() throws IOException {
        try (PrintWriter outWriter = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (!shouldClose){
                String message = inReader.readLine();

                if (new sResponse(outWriter).handleMessage(message) == 1) {
                    break;
                }

            }

            System.out.println("Socket Instance Closed");
        }  catch (Exception e) {
            System.out.println("Socket connection lost ...");
            userLoggedIn = null;
        }
    }
}

