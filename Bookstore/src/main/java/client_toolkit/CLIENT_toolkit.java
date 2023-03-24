package client_toolkit;

import file_toolkit.fTool;
import json_toolkit.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLIENT_toolkit {

    private static List<Book> allBooksList;
    private static List<Book> cartBooks = new ArrayList<Book>();
    private static List<Book> userBooks = new ArrayList<Book>();
    private static User userLoggedIn = null;
    public static Client client;

    public static void connectClient() throws IOException {
        client = new Client();
        client.connectSocket();
    }

    public void setUserLoggedIn(User user) {
        userLoggedIn = user;
    }

    public static void logOut() throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.LOG_OUT.getString());
        request.setMessageData(Collections.emptyList());

        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0) {
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e) {
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
            }
            if (response.getMessageType().equals(messageTypeEnum.SUCCESS.getString())) {
                // User successfully logged out
                System.out.println("User successfully logged out");
                userLoggedIn = null;
            }
        }
    }

    public static int logIn(String username, String password) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.LOG_IN.getString());
        User newUser = new User(username, password);
        request.setMessageData(Collections.singletonList(newUser));

        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return 6;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return 6;
            }
            if (response.getMessageType().equals(messageTypeEnum.PASSWORD_INCORRECT.getString())) {
                System.out.println("Password Incorrect! Please try again");
                return 1; // Incorrect Password
            } else if (response.getMessageType().equals(messageTypeEnum.USERNAME_NOT_FOUND.getString())) {
                System.out.println("Username not recognised");
                return 2; // Username not recognised
            }
            else if (response.getMessageType().equals(messageTypeEnum.SUCCESS.getString())){
                userLoggedIn = jTool.convertArrayList(response.getMessageData(), User.class).get(0);
                System.out.println("User successfully logged in:");
                cTool.printUser(userLoggedIn);
                return 0; // successful login
            }
        }
        return 1;
    }

    public void setAllBooks(List<Book> allBooks) {
        allBooksList = allBooks;
    }

    public static List<Book> getAllBooksList() {
        return allBooksList;
    }

    public static List<Book> getAllUserBooksList() { return userBooks; }

    public static List<Book> getCartBooks() {
        return cartBooks;
    }

    public static void clearCartBooks() {
        cartBooks.clear();
        System.out.println("Cart Cleared");
    }

    public static void setCartBooks(List<Book> cartBooksIn) {
        cartBooks = cartBooksIn;
    }

    public static void addBookToCart(Book book) {
        // See if there is already such a book in the cart ...
        if (cartBooks.isEmpty()) {
            // Book not yet in cart
            book.setStock(1);
            cartBooks.add(book);
            System.out.println("New book added to cart");
            return;
        }

        for (Book bookIdx : cartBooks) {
            if (book.getIsbn().equals(bookIdx.getIsbn())) {
                // Book exists in the cart
                bookIdx.setStock(bookIdx.getStock() + 1);
                System.out.println("Book in cart amount increased");
                break;
            } else {
                // Book not yet in cart
                book.setStock(1);
                cartBooks.add(book);
                System.out.println("New book added to cart");
                break;
            }
        }
    }

    public static void saveCart() throws Exception {
        try {
            System.out.println("Here now");
            fTool.saveToFile(new jTool<Book>().objectListToJSON(cartBooks));
        } catch (Exception e) {
            System.out.println("No save file selected! cart not saved ...");
            return;
        }
    }

    public static void loadCart() throws Exception {
        File cartFile = fTool.getFile();
        //System.out.println(Files.readString(cartFile.toPath()));
        List<Book> newCart;
        try {
            newCart = jTool.convertArrayList(new jTool<Book>().JSONtoObjectList(Files.readString(cartFile.toPath())), Book.class);
            System.out.println(newCart.getClass());
        } catch (JsonProcessingException e){
            System.out.println("Selected file not a cart ... please select a cart file");
            System.out.println("Message: " + Files.readString(cartFile.toPath()));
            loadCart();
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Null read from file ...");
            return;
        } catch (Exception e){
            System.out.println("No file selected ... cart not set!");
            return;
        }

        System.out.println("New cart set:");
        setCartBooks(newCart);
        cTool.printBookList(cartBooks);
    }

    public Book getBook(String isbn) {
        if (allBooksList == null) {
            System.out.println("Books not yet retrieved from server ... try: get_all_books");
            return new Book();
        }

        for (Book book : allBooksList){
            if (book.getIsbn().equals(isbn)){
                return book;
            }
        }
        System.out.println("Book not found!");
        return new Book();
    }

    public CLIENT_toolkit() throws IOException {
        //client.connectSocket();;
    }

    public static User getUser(Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.GET_USER.getString());
        request.setMessageData(Collections.emptyList());
        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0) {
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e) {
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return null;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return null;
            }
            if (response.getMessageType().equals(messageTypeEnum.NOT_SUCCESS.getString())) {
                System.out.println("No user logged in");
            } else {
                userLoggedIn = jTool.convertArrayList(response.getMessageData(), User.class).get(0);
                cTool.printUser(userLoggedIn);
                return userLoggedIn;
            }
        }
        return null;
    }

    public static int addUser(String username, String password, Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.ADD_NEW_USER.getString());
        User newUser = new User(username, password);
        request.setMessageData(Collections.singletonList(newUser));

        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return 6;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return 6;
            }
            if (response.getMessageType().equals(messageTypeEnum.PASSWORD_INCORRECT.getString())) {
                System.out.println("Password Incorrect! Please try again");
                return 1; // Incorrect Password
            } else if (response.getMessageType().equals(messageTypeEnum.USERNAME_NOT_FOUND.getString())) {
                System.out.println("Username already exists");
                return 2; // Username not recognised
            }
            else if (response.getMessageType().equals(messageTypeEnum.SUCCESS.getString())){
                userLoggedIn = newUser;
                System.out.println("User successfully logged in:");
                cTool.printUser(userLoggedIn);
                return 0; // successful login
            }
        }
        return 1;
    }

    public static void sellBooks(Book book, Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.SELL_BOOK.getString());
        request.setMessageData(new ArrayList<>(Collections.singletonList(book)));

        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return;
            }
            // Process the response
            if (response.getMessageType().equals(messageTypeEnum.SUCCESS.getString())) {
                System.out.println("Book successfully sold");
                CLIENT_toolkit.getUserBooks(CLIENT_toolkit.client);
                CLIENT_toolkit.getAllBooks(CLIENT_toolkit.client);
            }
        }
    }

    public static void getAllBooks(Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.GET_ALL_BOOKS.getString());
        request.setMessageData(Collections.emptyList());
        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return;
            }
            allBooksList = jTool.convertArrayList(response.getMessageData(), Book.class);
            cTool.printBookList(allBooksList);
        }
    }

    public static void buyCartBooks(Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.BUY_BOOKS.getString());
        request.setMessageData(new ArrayList<>(cartBooks));

        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return;
            }
            // Process the response
            if (response.getMessageType().equals(messageTypeEnum.SUCCESS.getString())) {
                System.out.println("Books successfully bought");
                clearCartBooks();
            }
        }
    }

    public static void getUserBooks(Client client) throws Exception {
        Message request = new Message();
        request.setMessageType(messageTypeEnum.GET_USER_BOOKS.getString());
        request.setMessageData(Collections.emptyList());
        if (client.sendMessage(new jTool<Message>().objectToJSON(request)) == 0){
            String responseString = Client.getResponse();
            System.out.println("Response received: " + responseString);

            Message response = new Message();

            try {
                response = jTool.JSONtoObject(responseString, Message.class);
            } catch (JsonProcessingException e){
                System.out.println("Message not received in Request Format ... Check Server Config");
                System.out.println("Message: " + responseString);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Null received from Server ... check connection and server settings");
                return;
            }
            userBooks = jTool.convertArrayList(response.getMessageData(), Book.class);
            cTool.printBookList(userBooks);
        }
    }
}
