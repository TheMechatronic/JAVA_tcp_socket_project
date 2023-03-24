package SERVER;

import json_toolkit.Book;
import json_toolkit.User;

import java.util.Arrays;
import java.util.List;

public class sTool {
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

    public static void printUser(User user) {
        System.out.println("ID: " + user.get_id());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Credits: " + user.getCredits());
        System.out.println(" ");
    }

    public static void printBookList(List<Book> books) {
        for (Book book: books) {
            printBook(book);
        }
    }


}
