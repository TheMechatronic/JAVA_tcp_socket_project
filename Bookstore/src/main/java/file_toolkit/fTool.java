package file_toolkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;

public class fTool {
    public static File getFile() {
        // create a File chooser
        FileChooser fileChooser = new FileChooser();

        // set title
        fileChooser.setTitle("Open File");

        // set initial File
        fileChooser.setInitialDirectory(new File("C:\\Users\\dylan\\OneDrive\\Desktop"));

        // get the file selected
        System.out.println("Should see file open window now");
        File file = fileChooser.showOpenDialog(null);

        return file;
    }

    public static void saveToFile(String textToSave) {
        System.out.println("Save file request received");
        // create a File chooser
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);


        System.out.println("Should see this now");
        // set title
        fileChooser.setTitle("Select File");

        // set initial File
        fileChooser.setInitialDirectory(new File("C:\\Users\\dylan\\OneDrive\\Desktop"));

        // get the file selected
        System.out.println("Should see file save window now");
        File file = fileChooser.showSaveDialog(null);

        // Add ".txt" extension if not already added
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the text to the file and close the writer
            writer.write(textToSave);
            writer.close();
            System.out.println("File saved: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


