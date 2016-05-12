package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{                         //abstract method in Application class
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));    //load the Xml of the sign in Scene
        primaryStage.setTitle("PC Chat Room");                                      //sitting the title
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();                                                        //showing the window which created before
    }

    public static void main(String[] args) {
        launch(args);                                                               //calling start method to start gui
    }
}
