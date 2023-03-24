package SERVER;

import java.io.IOException;
import java.util.Scanner;

public class launch_server {
    // Create a new Server object to use in the program
    static Server myServer = new Server();
    public static void main(String[] args) throws InterruptedException {
        // Start the server socket on port 2000
        myServer.startSocket(2000);

        // Wait for 500 milliseconds to give the server socket time to start
        Thread.sleep(500);

        // Prompt the user to enter a command
        System.out.println("Enter Command:\n'quit' to exit");
        Scanner scn = new Scanner(System.in);
        String s = scn.next();

        // Loop until the user enters the quit command
        while (true) {
            // If the user enters the quit or close command, close the server socket
            if ("quit".equals(s)) {
                try {
                    myServer.closeSocket();
                    System.out.println("Server Shutting Down");
                    System.exit(0);
                } catch (IOException e) {
                    // If the server socket could not be closed, print an error message and exit
                    System.err.println("Could not close port: 2000.");
                    System.exit(1);
                }
            }
            // Prompt the user to enter another command
            s = scn.next();
        }
    }
}
