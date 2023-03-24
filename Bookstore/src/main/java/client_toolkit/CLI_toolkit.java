package client_toolkit;

import json_toolkit.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI_toolkit {

    public static void startCLI() throws Exception {
        // New connection
        CLIENT_toolkit cToolkit = new CLIENT_toolkit();
        CLIENT_toolkit.connectClient();

        // Prompt the user to enter a command
        System.out.println("Enter Command:\n'quit' to exit\n'help' for more options");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        while ((userInput = consoleReader.readLine()) != null) {
            switch (userInput) {
                case "quit" -> {
                    try {
                        CLIENT_toolkit.client.closeSocket();
                        System.out.println("Program Shutting Down");
                        System.exit(0);
                    } catch (IOException e) {
                        // If the server socket could not be closed, print an error message and exit
                        System.err.println("Could not close port: 2000.");
                        System.exit(1);
                    }
                }
                case "help" -> {
                    System.out.println("List of implemented commands:");
                    System.out.println("'quit'\t\t\t\t- Close Program");
                    System.out.println("'connect'\t\t\t- Start Connection to Server");
                    System.out.println("'get_all_books'\t\t- Get all Books from the Server");
                    System.out.println("'show_all_books'\t- Display all Books");
                    System.out.println("'add_to_cart'\t\t- Add book to Cart");
                    System.out.println("'show_cart'\t\t\t- Show books in Cart");
                    System.out.println("'save_cart'\t\t\t- Save current cart");
                    System.out.println("'clear_cart'\t\t- Clear current Cart");
                    System.out.println("'load_cart'\t\t\t- Load a saved Cart");
                    System.out.println("'buy_cart'\t\t\t- Buy the books in the Cart");
                    System.out.println("'log_in'\t\t\t- New user log in");
                    System.out.println("'get_user_books'\t- Get the books owned by current user");
                    System.out.println("'show_user_books'\t- Show the books owned by the user");
                    System.out.println("'get_user'\t\t\t- Get the current user");
                }
                case "connect" -> CLIENT_toolkit.client.connectSocket();
                case "get_all_books" -> CLIENT_toolkit.getAllBooks(CLIENT_toolkit.client);
                case "show_all_books" -> cTool.printBookList(CLIENT_toolkit.getAllBooksList());
                case "sell_book" -> {
                    String isbn;
                    Book book;
                    System.out.println("Enter ISBN to continue: ('cancel' to go back)");
                    while ((isbn = consoleReader.readLine()) != null) {
                        if (isbn.equals("cancel")) {
                            // Cancel command received
                            System.out.println("Enter Command:\n'quit' to exit\n'help' for more options");
                            break;
                        } else {
                            // Check to se if book isbn exists ... then add to cart
                            book = cToolkit.getBook(isbn);
                            if (book.getTitle() == null) {
                                System.out.println("Please enter valid isbn number:");
                            } else if (book.getTitle().isEmpty()) {
                                System.out.println("Please enter valid isbn number:");
                            } else {
                                System.out.println("Adding: " + book.getTitle() + " to Cart");
                                CLIENT_toolkit.sellBooks(book, CLIENT_toolkit.client);
                                break;
                            }
                        }
                    }
                }
                case "save_cart" -> CLIENT_toolkit.saveCart();
                case "add_to_cart" -> {
                    String isbn;
                    Book book;
                    System.out.println("Enter ISBN to continue: ('cancel' to go back)");
                    while ((isbn = consoleReader.readLine()) != null) {
                        if (isbn.equals("cancel")) {
                            // Cancel command received
                            System.out.println("Enter Command:\n'quit' to exit\n'help' for more options");
                            break;
                        } else {
                            // Check to se if book isbn exists ... then add to cart
                            book = cToolkit.getBook(isbn);
                            if (book.getTitle() == null) {
                                System.out.println("Please enter valid isbn number:");
                            } else if (book.getTitle().isEmpty()) {
                                System.out.println("Please enter valid isbn number:");
                            } else {
                                System.out.println("Adding: " + book.getTitle() + " to Cart");
                                CLIENT_toolkit.addBookToCart(book);
                                break;
                            }
                        }
                    }
                }
                case "show_cart" -> cTool.printBookList(CLIENT_toolkit.getCartBooks());
                case "clear_cart" -> CLIENT_toolkit.clearCartBooks();
                case "load_cart" -> CLIENT_toolkit.loadCart();
                case "log_in" -> {
                    // Get the username and password from the user
                    String username;
                    String password = null;
                    int login = 1;

                    // Get username and password from user:
                    do {
                        System.out.println("Enter Username: ");
                        username = consoleReader.readLine();
                        if (username.equals("")) {
                            System.out.println("Username cannot be empty");
                        } else if (username.equals("cancel")) {
                            System.out.println("Enter Command:\n'quit' to exit\n'help' for more options");
                            login = 0;
                            break;
                        } else {
                            System.out.println("Enter Password: ");
                            password = consoleReader.readLine();
                        }

                        login = CLIENT_toolkit.logIn(username, password);
                    } while (login != 0);

                }
                case "get_user_books" -> {
                    CLIENT_toolkit.getUserBooks(CLIENT_toolkit.client);
                }
                case "show_user_books" -> cTool.printBookList(CLIENT_toolkit.getAllUserBooksList());
                case "get_user" -> CLIENT_toolkit.getUser(CLIENT_toolkit.client);
                case "buy_cart" -> CLIENT_toolkit.buyCartBooks(CLIENT_toolkit.client);
                default -> { //client.sendMessage(userInput); }
                }
            }
        }
    }
}
