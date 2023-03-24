package server_toolkit;

import SERVER.sTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import json_toolkit.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static SERVER.ClientConnectionHandler.userLoggedIn;
import static server_toolkit.dbTool.getBook;

public class sResponse {
    private PrintWriter printWriter;
    private String messageIN;
    private Message message;

    public sResponse(PrintWriter out) {
        printWriter = out;
    }

    public int handleMessage(String msg) throws Exception {

        System.out.println("Message Received: " + msg);

        try {
            message = jTool.JSONtoObject(msg, Message.class);
        } catch (JsonProcessingException e){
            /* DEBUG: */
            System.out.println("Message received ... Message not in Request Format: " + msg);
            return EchoServer(msg);
        }


        /* DEBUG: */
        System.out.println("Message received ... Request Type: " + message.getMessageType());

        switch (message.getMessageType()) {
            case "ECHO" -> {
                return EchoServer(msg);
            }
            case "GET_ALL_BOOKS" -> {
                System.out.println("GET_ALL_BOOKS request received");
                return sendAllBooks();
            }
            case "SELL_BOOK" -> {
                System.out.println("SELL_BOOK request received");
                sellBooks();
                return 0;
            }
            case "LOG_IN" -> {
                System.out.println("LOG_IN request received");
                userLogin();
            }
            case "BUY_BOOKS" -> {
                System.out.println("BUY_BOOKS request received");
                buyBooks();
            }
            case "GET_USER_BOOKS" -> {
                System.out.println("GET_USER_BOOKS request received");
                sendUserBooks();
            }
            case "GET_USER" -> {
                System.out.println("GET_USER request received");
                sendUser();
            }
            case "ADD_NEW_USER" -> {
                System.out.println("ADD_NEW_USER request received");
                newUser();
            }
            case "LOG_OUT" -> {
                System.out.println("LOG_OUT request received");
                userLogout();
            }
            default -> {
            }
        }
        return 0;
    }

    /*
     EchoServer just returns the received message to the client
     returns 1 if client requests quit
     returns 0 otherwise
    */
    public int EchoServer(String msg) {
        System.out.println("EchoServer Received: " + msg);
        if (msg.equals("Hello Server")){
            printWriter.println("Hello Client");
        } else {
            printWriter.println(msg);
        }
        return 0;
    }

    public int sendAllBooks() throws Exception {
        // Get array list of books
        ArrayList<Object> booksObj = new ArrayList<>(dbTool.getAllBooks("Books"));

        // Create request with these books:
        Message response = new Message();
        response.setMessageData(booksObj);
        response.setMessageType(requestType.SUCCESS.getString());
        printWriter.println(new jTool<Message>().objectToJSON(response));
        return 0;
    }

    public void sendUserBooks() throws Exception {

        // No user logged in
        if (userLoggedIn == null) {
            System.out.println("No user signed in ...");
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.NOT_SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
            return;
        }

        // Get array list of books
        ArrayList<Object> booksObj = new ArrayList<>(dbTool.getAllBooks(userLoggedIn.getUsername()));

        // Create request with these books:
        Message response = new Message();
        response.setMessageData(booksObj);
        response.setMessageType(requestType.SUCCESS.getString());
        printWriter.println(new jTool<Message>().objectToJSON(response));
    }

    public void sellBooks() throws Exception {
        Book bookToSell = jTool.convertArrayList(message.getMessageData(), Book.class).get(0);
        Book dbBook = dbTool.getBookISBN(bookToSell.getIsbn(), "Books");
        Book userBook = dbTool.getBookISBN(bookToSell.getIsbn(), userLoggedIn.getUsername());

        // Check if the user owns the book
        if (userBook == null) {
            // User does not own the book

            // Send the !success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.NOT_SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }

        else {
            // Update the main database book
            dbBook.setStock(dbBook.getStock() + 1);
            dbTool.updateBook(dbBook, "Books");

            // Update the User books
            if (userBook.getStock() == 1) {
                // The last book that the user owns has been sold
                dbTool.removeBook(userBook, userLoggedIn.getUsername());
            } else {
                // Reduce the stock of the user
                userBook.setStock(userBook.getStock() - 1);
                dbTool.updateBook(userBook, userLoggedIn.getUsername());
            }

            // Increase the user credits
            userLoggedIn.setCredits(userLoggedIn.getCredits() + bookToSell.getPrice());
            dbTool.updateUser(userLoggedIn);

            // Send the success string to the client
            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }

    }

    public void newUser() throws Exception {
        User newUser = jTool.convertArrayList(message.getMessageData(), User.class).get(0);

        // Login request received
        System.out.println("New user login request received");

        // Convert user from request data ...
        // TODO delete this after debugging
        sTool.printUser(newUser);

        // Get the user from the database with the username:
        User dbUser = dbTool.getUser(newUser.getUsername());

        // Check if the user exists in the database
        if (dbUser == null){
            // Username has not been found in the database -- valid
            System.out.println("Username is new - add user to database");
            System.out.println("Username: " + newUser.getUsername());

            newUser.setCredits(1000);
            dbTool.addNewUser(newUser);
            userLoggedIn = newUser;

            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        } else {
            // Username is not unique ... send username error to client
            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.USERNAME_NOT_FOUND.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }
    }

    public void buyBooks() throws Exception {
        // Get the list of books to buy from the request!
        List<Book> booksToBuy = jTool.convertArrayList(message.getMessageData(), Book.class);

        // Calculate the cart total ...
        double total = 0;
        for (Book book : booksToBuy) {
            total += book.getStock()*book.getPrice();
        }

        // Check to see if user can afford the books
        if (userLoggedIn.getCredits() < total) {
            // User cannot afford the books
            System.out.println("User cannot afford the requested books");

            // TODO return credits failure to client
        }

        // Check to see if there is stock of the books and then sell these that have ...
        // create a list of the books that were not able to be bought due to stock
        List<Book> failedBooks = new ArrayList<Book>();

        for (Book book : booksToBuy) {
            Book dbBook = dbTool.getBookISBN(book.getIsbn(), "Books");

            // Debug prints
            sTool.printBook(book);
            sTool.printBook(dbBook);

            // if book requested stock > book stock sell the books
            if (dbBook == null) {
                System.out.println("Book not in database!!");
            } else if (book.getStock() <= dbBook.getStock()){
                // Stock available
                System.out.println("Book bought ... ");

                // Get the book from the user database
                Book userBook = getBook(dbBook.get_id(), userLoggedIn.getUsername());

                // Check if the book is already in the user database
                if (userBook != null) {
                    // User already owns this book
                    System.out.println("User owns a copy of this book ... add stock");

                    // Update user database
                    userBook.setStock(userBook.getStock() + book.getStock());
                    dbTool.updateBook(userBook, userLoggedIn.getUsername());

                    // Reduce stock in main
                    dbBook.setStock(dbBook.getStock() - book.getStock());
                    dbTool.updateBook(dbBook, "Books");

                } else {
                    // Reduce stock in main
                    dbBook.setStock(dbBook.getStock() - book.getStock());
                    dbTool.updateBook(dbBook, "Books");

                    // Book not yet in user database
                    userBook = dbBook;
                    userBook.setStock(book.getStock());

                    // Adjust the user database
                    dbTool.addNewBook(userBook, userLoggedIn.getUsername());
                }

                // Reduce credits of user
                // Adjust the user credits
                userLoggedIn.setCredits(userLoggedIn.getCredits() - (book.getStock()*book.getPrice()));
                dbTool.updateUser(userLoggedIn);

            } else {
                // No stock available
                failedBooks.add(book);
            }
        }

        // send the response back to the client
        if (failedBooks.isEmpty()) {
            // no problems with stock
            System.out.println("All books successfully bought");

            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }

        // TODO send failure message to client along with books out of stock

    }

    public void sendUser() throws Exception {
        if (userLoggedIn == null) {
            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.NOT_SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        } else {
            // return the user logged in
            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.singletonList(userLoggedIn));
            response.setMessageType(requestType.SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }
    }

    public void userLogin() throws Exception {
        // Login request received
        System.out.println("New user login request received");

        // Convert user from request data ...
        User user = jTool.convertArrayList(message.getMessageData(), User.class).get(0);
        // TODO delete this after debugging
        sTool.printUser(user);

        // Get the user from the database with the username:
        User dbUser = dbTool.getUser(user.getUsername());

        // Check if the user exists in the database
        if (dbUser == null){
            // Username has not been found in the database
            System.out.println("Username not found!");
            System.out.println("Username: " + user.getUsername());

            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.USERNAME_NOT_FOUND.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }
        else if ((dbUser.getPassword().equals(user.getPassword()))){
            // Password matches
            System.out.println("User successfully logged in!");
            System.out.println("User: " + user.getUsername());

            // Log in the new user!
            userLoggedIn = dbUser;

            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.singletonList(dbUser));
            response.setMessageType(requestType.SUCCESS.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));

        } else {
            // Password does not match
            System.out.println("Passwords do not match!");

            // Send the success code and user class back to the client
            Message response = new Message();
            response.setMessageData(Collections.emptyList());
            response.setMessageType(requestType.PASSWORD_INCORRECT.getString());
            printWriter.println(new jTool<Message>().objectToJSON(response));
        }
    }

    public void userLogout() throws Exception {
        userLoggedIn = null;
        System.out.println("User logged out");
        // Send the success code and user class back to the client
        Message response = new Message();
        response.setMessageData(Collections.emptyList());
        response.setMessageType(requestType.SUCCESS.getString());
        printWriter.println(new jTool<Message>().objectToJSON(response));
    }
}

