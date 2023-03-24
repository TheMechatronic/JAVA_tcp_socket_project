package SERVER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class Acception {

    // Declare serverSocket and closeSocket as public static variables
    public static ServerSocket serverSocket;
    public static boolean closeSocket = false;

    // Constructor for the Acception class
    public Acception(ServerSocket newServerSocket) {
        // Set the serverSocket to the newServerSocket passed as an argument
        serverSocket = newServerSocket;
        // Start the acception thread
        acception.start();
    }

    // Method to close the socket
    public static void CloseSocket() throws IOException {
        System.out.println("Closing Socket Instance");
        // Set shouldClose to true for all client connection handlers
        ClientConnectionHandler.shouldClose = true;
        // Set closeSocket to true to stop the acception thread from accepting new connections
        closeSocket = true;
        // Close the serverSocket
        serverSocket.close();
        System.out.println("Socket Instance Closed");
    }

    // Create a new thread named acception
    static final Thread acception = new Thread("Acception Thread") {
        // Override the run method
        public void run() {
            try {
                // Loop until closeSocket is true
                System.out.println("New 'Acception' thread started");
                System.out.println("Waiting for Connection on Port: " + serverSocket.getLocalPort());
                while (!closeSocket) {
                    // Accept a new connection and create a new ClientConnectionHandler thread for it
                    new ClientConnectionHandler(serverSocket.accept());
                }
            } catch (SocketException e) {
                // If the serverSocket is closed, print a message indicating that the connection was closed
                if (serverSocket.isClosed())
                    System.out.println("Connection Closed.");
            } catch (IOException e) {
                // If the accept call fails, print an error message and exit
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
    };
}
