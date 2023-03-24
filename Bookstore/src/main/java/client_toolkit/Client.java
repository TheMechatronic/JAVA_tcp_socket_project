package client_toolkit;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {

    // Initialize instance variables
    private final int portNumber = 2000;
    private final InetAddress host = InetAddress.getLocalHost();
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    // Constructor
    public Client() throws IOException {

    }

    // Sends a message to the server, with an optional flag to wait for a response
    public int sendMessage(String message) throws IOException, InterruptedException {
        if (socket != null) {
            if (socket.isConnected()) {
                printWriter.println(message);
                return 0;
            } else {
                System.out.println("Server no longer connected ... message not sent");
            }
        } else {
            System.out.println("No connection ... message not sent");
            return 1;
        }
        return 1;
    }

    // Waits for a response from the server with timeout
    static String getResponse() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);

        String response;
        try {
            response = future.get(25, TimeUnit.SECONDS);
        } catch (Exception e) {
            response = null;
        } finally {
            future.cancel(true);
            executor.shutdownNow();
        }
        return response;
    }

    // Establishes a socket connection to the server
    public void connectSocket() throws IOException {
        try{
            socket = new Socket(host, portNumber);
            input = new DataInputStream(socket.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Socket connection established at host: " + host + ", port: " + portNumber);
        } catch (IOException e){
            System.out.println("No open socket found on port: " + portNumber);
        }
    }

    // Closes the socket connection
    public void closeSocket() throws IOException {
        if (socket != null){
            output.flush();
            output.close();
            socket.close();
            System.out.println("Socket Closed ...");
        }
    }
}
