package client_toolkit;

import json_toolkit.Book;
import json_toolkit.User;

import java.util.Arrays;
import java.util.List;

public class cTool {

    public static void printBook(Book book) {
        System.out.println("ID: " + book.get_id());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Title: " + book.getTitle());
        System.out.println("Authors: " + Arrays.toString(book.getAuthors()));
        System.out.println("Year: " + book.getYear());
        System.out.println("Stock: " + book.getStock());
        System.out.println("Price: " + book.getPrice());
        System.out.println(" ");
    }

    public static void printBookList(List<Book> books) {
        if (books == null) System.out.println("No Books to Display ... try: 'get_all_books'");
        else {
            for (Book book : books) {
                printBook(book);
            }
        }
    }

    public static void printUser(User user) {
        System.out.println("ID: " + user.get_id());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Credits: " + user.getCredits());
        System.out.println(" ");
    }

}
