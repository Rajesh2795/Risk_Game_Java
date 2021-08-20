package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.model.GameDetails;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This NewMapController class is responsible for controlling the newmap window
 * This class creates the Textarea to construct the newmap
 * and saves the map if the map is valid. Otherwise displays map is not valid.
 * @author Team43
 */
public class NewMapController {

    @FXML
    private Button newmapSavebutton,newmapCancelbutton;

    @FXML
    private BorderPane NewMapBorderPane;

    @FXML
    private TextArea newmapTextarea,mapformatTextarea;

    private MapEditorController meController = new MapEditorController();

    private String filePath = "E:\\IntelliJ\\AppProject\\src\\resources\\ModifiedMaps\\";

    private String fileName;

    private static int count = 1;

    @FXML
    private TextField mapnameTextfield;

    /**
     * To set the map format instructions to the textarea.
     */
    @FXML
    public void initialize() {

        String mapInstruction = "[Map]\n" + "image=world.bmp\n" + "wrap=yes\n" + "scroll=horizontal\n" +
                "author=Your name\n" + "warn=yes\n" + "\n" + "The second section describes the Continent names and the " +
                "scores for owning them completely.\n" + "\n" + "[Continents]\n" + "North America=5\n" + "South America=2\n" +
                "Africa=3\n" + "Europe=5\n" + "Asia=7\n" + "Australia=2\n" + "\n" + "The final section describes the " +
                "territories. The first parameter is the territory's name, then the \"x\" and \"y\" coordinates of the " +
                "center of the territory in pixels from the top left corner of the bitmap, then the continent that the " +
                "territory is in and finally list all the territories that this territory connects to. The maximum " +
                "number of continents that a map can have is 32\n" + "\n" + "[Territories]\n" + "Siam,270,139,Asia," +
                "China,India,Indonesia\n" + "Japan,322,104,Asia,Kamchatka,Mongolia\n" + "Ural,241,68,Asia,Siberia," +
                "China,Afghanistan,Ukraine\n" + "etc...\n" + "\n" + "The maximum number of territories that any territory " +
                "can connect to is 10. The maximum number of territories that a map can have is 255.";

        mapformatTextarea.setText(mapInstruction);
    }

    /**
     * To save the contents from the text area and verify whether it is valid map or not.
     */
    public void saveButtonAction() {

        // If text area is not empty then read the contents to the file and check map is valid or not.
        if(!newmapTextarea.getText().trim().isEmpty()) {
            File file = new File("NewMap.map");

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                writer.write(newmapTextarea.getText());
                writer.flush();
                writer.close();

                boolean result = meController.ismapValid(file,"NEWMAP");

                if(result) {
                    // Check if the map name is empty. If empty then give some default name.
                    if(mapnameTextfield.getText().trim().isEmpty()) {
                        fileName = "NewMap"+count;
                        count++;
                    } else {
                        fileName = mapnameTextfield.getText().trim();
                    }

                    // Create file and write the map contents to the file and store.
                    Path path = Paths.get(filePath+fileName+".map");
                    try {
                        writer = Files.newBufferedWriter(path);
                        writer.write(newmapTextarea.getText());
                        writer.flush();

                        System.out.println("Map is valid. Successfully saved the map.\nMap saved location foldername =" +
                                          " Modifiedmaps");
                    } finally {
                        if(writer != null) {
                            writer.close();
                            cancelButtonAction();
                        }
                    }
                } else {
                    System.out.println("Map is not valid. Please check the continents and territories and try again.");

                    // Clear the data.
                    GameDetails.getGamedetails().clearData();

                    // Close the window.
                    cancelButtonAction();
                }

            } catch (Exception e) {
                System.out.println("Cannot write the contents to the file.");
                e.printStackTrace();
                return;
            }
        } else {
            System.out.println("Text area is empty.\nCannot validate the map.\nError.");
            cancelButtonAction();
        }
    }

    /**
     * To cancel the new map window.
     */
    public void cancelButtonAction() {

        GameDetails.getGamedetails().clearData();
        Stage stage = (Stage) newmapCancelbutton.getScene().getWindow();
        stage.close();
    }

}
