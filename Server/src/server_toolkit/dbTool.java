package server_toolkit;

import SERVER.sTool;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import json_toolkit.Book;
import json_toolkit.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dbTool {

    private static final String DATABASE_NAME = "Holonics";
    private static final String COLLECTION_NAME = "Books";

    public static void userBuyBook(User user, Book book) {
        // Get the main db version of the book from isbn
        Book mainDbBook = getBookISBN(book.getIsbn(), "Books");
        
        // Check that the db Book exists ... otherwise invalid buy order
        if (mainDbBook == null) {
            System.out.println("Illegal buy order");
            return;
        }
        
        // Get the book from the user database
        Book userBook = getBook(mainDbBook.get_id(), user.getUsername());
        
        // Check if the book is already in the user database
        if (userBook != null) {
            // User already owns this book
            userBook.setStock(userBook.getStock() + book.getStock());
        } else {
            // Book not yet in user database
            userBook = mainDbBook;
            userBook.setStock(book.getStock());
        }
        
        // Reduce the main database stock
        mainDbBook.setStock(mainDbBook.getStock() - book.getStock());
        
        // Adjust both books
        updateBook(userBook, user.getUsername());
        updateBook(mainDbBook, "Books");
        
        // Adjust the user credits
        user.setCredits(user.getCredits() - (book.getStock()*book.getPrice()));
        updateUser(user);

        // Send back the success code

    }

    // Passed Tests
    public static void addNewUser(User user) {
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> userCollection = database.getCollection("User_profiles");

        // check if the book exists in the collection
        Document query = new Document("username", user.getUsername());
        Document result = userCollection.find(query).first();

        if (result != null) {
            System.out.println("User already exists in the database.");
            // TODO Add method for user already existing in the database
        } else {
            // if the user does not exist, insert it into the collection
            Document doc = new Document("_id", user.get_id())
                    .append("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("credits", user.getCredits());

            sTool.printUser(user);
            userCollection.insertOne(doc);
            System.out.println("User inserted into the database.");

            database.createCollection(user.getUsername());
            System.out.println("New user Database Created");
        }
    }

    public static int updateUser(User user) {
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection("User_profiles");

        // check if the book exists in the collection
        Document query = new Document("_id", user.get_id());

        // Create a filter to specify which book document to update
        Bson filter = Filters.eq("_id", user.get_id());

        // Create an update to modify the value of the title field
        Bson update = Updates.combine(
                Updates.set("credits", user.getCredits()),
                Updates.set("password", user.getPassword())
        );

        // Check if the book exists
        if (booksCollection.find(query).first() != null) {
            // Book exists - Use the updateOne() method to update the book document
            booksCollection.updateOne(filter, update);
            System.out.println("User Updated.");
            System.out.println(user.getUsername());
            System.out.println(" ");
            return 0;
        } else {
            System.out.println("User not found.");
            System.out.println(" ");
            return 1;
        }
    }

    // Passed Tests
    public static User getUser(String username){
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> userCollection = database.getCollection("User_profiles");

        // check if the book exists in the collection
        Document query = new Document("username", username);

        // Check if the user exists
        Document doc = userCollection.find(query).first();
        if (doc != null) {
            // User exists, return it
            User user = new User(
                    doc.getObjectId("_id"),
                    doc.getString("username"),
                    doc.getString("password"),
                    doc.getDouble("credits")
            );

            System.out.println("User found:");
            System.out.println("username:" + user.getUsername());
            System.out.println(" ");
            return user;
        } else {
            System.out.println("User not found.");
            System.out.println("username: " + username);
            System.out.println(" ");
        }
        return null;
    }

    // Passed Tests
    public static Book getBook(ObjectId id, String collection){
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // check if the book exists in the collection
        Document query = new Document("_id", id);

        // Check if the book exists
        Document doc = booksCollection.find(query).first();
        if (doc != null) {
            // Book exists, return it
            // Extract the array from the document and cast it to a List of String objects
            List<?> array = (List<?>) doc.get("authors");

            // Create a new String array and copy the elements from the List
            String[] stringArray = new String[array.size()];
            array.toArray(stringArray);

            Book book = new Book(
                    doc.getObjectId("_id"),
                    doc.getString("isbn"),
                    doc.getString("title"),
                    stringArray,
                    doc.getInteger("year"),
                    doc.getInteger("stock"),
                    doc.getDouble("price")
            );

            System.out.println("Book found:");
            System.out.println(book.getTitle());
            System.out.println(" ");
            return book;
        } else {
            System.out.println("Book not found.");
            System.out.println("_id: " + id);
            System.out.println(" ");
        }
        return null;
    }

    // Passed Tests
    public static Book getBookISBN(String ISBN, String collection){
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // check if the book exists in the collection
        Document query = new Document("isbn", ISBN);

        // Check if the book exists
        Document doc = booksCollection.find(query).first();
        if (doc != null) {
            // Book exists, return it
            // Extract the array from the document and cast it to a List of String objects
            List<?> array = (List<?>) doc.get("authors");

            // Create a new String array and copy the elements from the List
            String[] stringArray = new String[array.size()];
            array.toArray(stringArray);;

            Book book = new Book(
                    doc.getObjectId("_id"),
                    doc.getString("isbn"),
                    doc.getString("title"),
                    stringArray,
                    doc.getInteger("year"),
                    doc.getInteger("stock"),
                    doc.getDouble("price")
            );

            System.out.println("Book found:");
            System.out.println(book.getTitle());
            System.out.println(" ");
            return book;
        } else {
            System.out.println("Book not found.");
            System.out.println("ISBN: " + ISBN);
            System.out.println(" ");
        }
        return null;
    }

    // Passed Tests
    public static int updateBook(Book book, String collection) {
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // check if the book exists in the collection
        Document query = new Document("_id", book.get_id());

        // Create a filter to specify which book document to update
        Bson filter = Filters.eq("_id", book.get_id());

        // Create an update to modify the value of the title field
        Bson update = Updates.combine(
                Updates.set("title", book.getTitle()),
                Updates.set("authors", Arrays.asList(book.getAuthors())),
                Updates.set("isbn", book.getIsbn()),
                Updates.set("price", book.getPrice()),
                Updates.set("stock", book.getStock()),
                Updates.set("year", book.getYear())
        );

        // Check if the book exists
        if (booksCollection.find(query).first() != null) {
            // Book exists - Use the updateOne() method to update the book document
            booksCollection.updateOne(filter, update);
            System.out.println("Book Updated.");
            System.out.println(book.getTitle());
            System.out.println(" ");
            return 0;
        } else {
            System.out.println("Book not found.");
            System.out.println(" ");
            return 1;
        }
    }

    // Passed Tests
    public static void removeBook(Book book, String collection) {
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // check if the book exists in the collection
        Document query = new Document("isbn", book.getIsbn());

        // Check if the book exists
        if (booksCollection.find(query).first() != null) {
            // Book exists, delete it
            booksCollection.deleteOne(query);
            System.out.println("Book deleted.");
            System.out.println(book.getTitle());
            System.out.println(" ");
        } else {
            System.out.println("Book not found.");
            System.out.println(" ");
        }
    }

    // Passed Tests
    public static void addNewBook(Book book, String collection) {
        // create a MongoDB client and get the database and collection
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // check if the book exists in the collection
        Document query = new Document("isbn", book.getIsbn());
        Document result = booksCollection.find(query).first();

        if (result != null) {
            System.out.println("Book already exists in the database.");
            // TODO Add method for book already existing in the database
        } else {
            // if the book does not exist, insert it into the collection
            Document doc = new Document("_id", book.get_id())
                    .append("isbn", book.getIsbn())
                    .append("title", book.getTitle())
                    .append("authors", Arrays.asList(book.getAuthors()))
                    .append("year", book.getYear())
                    .append("stock", book.getStock())
                    .append("price", book.getPrice());
            sTool.printBook(book);
            booksCollection.insertOne(doc);
            System.out.println("Book inserted into the database.");
        }
    }

    // Passed tests ...
    public static List<Book> getAllBooks(String collection) {
        // Connect to the MongoDB database
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("Holonics");

        // Get the "books" collection
        MongoCollection<Document> booksCollection = database.getCollection(collection);

        // Find all documents in the "books" collection
        MongoCursor<Document> cursor = booksCollection.find().iterator();

        // create an empty list to store the Book objects
        List<Book> books = new ArrayList<>();

        // iterate over all the documents in the collection and convert them to Book objects
        while (cursor.hasNext()) {
            Document doc = cursor.next();

            // Extract the array from the document and cast it to a List of String objects
            List<?> array = (List<?>) doc.get("authors");

            // Create a new String array and copy the elements from the List
            String[] stringArray = new String[array.size()];
            array.toArray(stringArray);;

            Book book = new Book(
                    doc.getObjectId("_id"),
                    doc.getString("isbn"),
                    doc.getString("title"),
                    stringArray,
                    doc.getInteger("year"),
                    doc.getInteger("stock"),
                    doc.getDouble("price")
            );
            books.add(book);
        }

        // close the cursor and MongoDB client
        cursor.close();

        return books;
    }

}
