package SERVER;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    // Declare serverSocket as a static variable initialized to null
    static ServerSocket serverSocket = null;

    // Method to start the server socket on the specified port number
    public void startSocket(int portNumber) {
        try {
            // Create a new server socket on the specified port number
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Connection Socket Created on Port: " + portNumber);
            // Create a new Acception object to start accepting client connections
            new Acception(serverSocket);
        } catch (IOException e) {
            // If the server socket could not be created, print an error message and exit
            System.err.println("Could not listen on port: " + portNumber + ".");
            System.exit(1);
        }
    }

    // Method to check if the server socket is running
    public boolean isRunning() {
        // Return true if the server socket is not closed
        return !Acception.serverSocket.isClosed();
    }

    // Method to close the server socket
    public void closeSocket() throws IOException {
        // Call the CloseSocket method of the Acception class to close the server socket
        Acception.CloseSocket();
    }
}

