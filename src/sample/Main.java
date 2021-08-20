package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is the main class.
 * This class runs the main GUI window
 */
public class Main extends Application {

    /**
     * This method sets the stage and load the main window to the console.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/sample.fxml"));
        primaryStage.setTitle("Risk Game");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This is the main method.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
