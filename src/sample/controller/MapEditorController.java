package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.GameDetails;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class displays the text area for the user to edit the map.
 * This class displays the file chooser to choose the map and then edit.
 * After editing the map it is validated if the map is valid then it is saved otherwise
 * reports the error to the user and closes the window.
 * @author Team43
 */
public class MapEditorController {

    @FXML
    private TextArea textArea;

    private File selectedfile;
    @FXML
    private AnchorPane anchorPane;

    private StringBuilder builder = new StringBuilder();

    @FXML
    private Button saveButton;

    @FXML
    private Button closeButton;

    private File file;

    private String filePath = "E:\\IntelliJ\\AppProject\\src\\resources\\ModifiedMaps\\";

    private Controller controller = new Controller();

    /**
     * To choose the map.
     */
    public void chooseMap() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Map");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        selectedfile = chooser.showOpenDialog(anchorPane.getScene().getWindow());

        // Check if the Map is valid or not.
        // If valid print map is valid  else print invalid map.
        if(ismapValid(selectedfile,"EDITMAP")) {
            System.out.println("**********************************Map is valid.**************************************");
        } else {
            System.out.println("**************************Invalid map check the connections.*************************");
        }

        // Clear the object created for checking map validation.
        GameDetails.getGamedetails().clearData();

        // read the contents from the file and display to the textarea.
        readFilecontents();
        textArea.setText(builder.toString());
    }

    /**
     * To read the contents from a file.
     */
    public void readFilecontents() {

        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedfile));
            while((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch(Exception e) {
            System.out.println("Could not load the contents from the file");
            e.printStackTrace();
            return;
        }
    }

    /**
     * To function saves performs the map validation after editing the map and then if the map is valid then
     * the map is saved otherwise the error is reported to the user and closes the window.
     */
    public void saveContent() {

        file = new File(selectedfile.getName());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(textArea.getText());
            writer.flush();
            writer.close();

            boolean result = ismapValid(file, "EDITMAP");
            if(result) {
                System.out.println("Map is Valid and successfully saved.");
                Path path = Paths.get(filePath+selectedfile.getName());
                try {
                    writer = Files.newBufferedWriter(path);
                    writer.write(textArea.getText());
                    writer.flush();
                } finally {
                    if(writer != null) writer.close();
                    cancelAction();
                }

            } else {
                System.out.println("Invalid Map. Remodify the map.");

                // Clear the data.
                GameDetails.getGamedetails().clearData();

                // Closes the window
                cancelAction();
            }

        } catch(Exception e) {
            System.out.println("Cannot write contents to the file.");
            e.printStackTrace();
            return;
        }
    }

    /**
     * To close the Map editor window.
     */
    public void cancelAction() {

        Stage stage = (Stage) closeButton.getScene().getWindow();
        GameDetails.getGamedetails().clearData();
        stage.close();
    }

    /**
     * This function checks whether map is valid or not and return true if map is valid or false.
     * @param file
     * @param typename
     * @return result
     */
    public boolean ismapValid(File file, String typename) {

        GameDetails.getGamedetails().getgamedetails().add(new GameDetails(file, typename));

        // To get the index of the Game details object of type map editor.
        int index = controller.returnIndex(typename);

        GameDetails.getGamedetails().getgamedetails().get(index).createMap(index);
        boolean result = GameDetails.getGamedetails().getgamedetails().get(index).validateMap(index);
        System.out.println("Is Map Valid function result is = " + result);

        return result;
    }
}
