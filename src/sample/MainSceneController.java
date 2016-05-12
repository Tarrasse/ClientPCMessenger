package sample;

import Users.User;
import Users.UserMaker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainSceneController implements Initializable{
    private UserMaker userMaker = new UserMaker();

    private Socket socket ;
    private PrintWriter output ;
    private Scanner input ;
    private String msg;

    public VBox root;
    public TextField userNameTextField;
    public Button startButton;
    public Label warning ;                                                          //existing label but text is empty
    public CheckBox Male;
    public CheckBox Female;

    boolean male , female, selection ;

    //on GoButton click
    public void start (){
        //get the text which user enter
        msg = userNameTextField.getText();

        male = Male.isSelected();
        female= Female.isSelected();


        if (male){
            selection = male;
        }else{
            selection= female;
        }

        if(msg == null || msg.trim().length() < 4){

            warning.setText("please enter name > 3 chars");

        }else{
            //setting the user name
            Data.USERNAME=msg;

            try {
                socket= new Socket(Data.HOST, Data.PORT);

                //initializing the PrintWriter and the Scanner
                output = new PrintWriter(socket.getOutputStream(),true);
                input= new Scanner(socket.getInputStream());

                //sending the name to the server
                output.println(Data.USERNAME);

                //getting the server response if name available "1" else "0"
                String msg = input.nextLine();

                if(msg.equals(String.valueOf("1"))) {                               //if the name available

                    User user = userMaker.makeUser(selection);

                    user.setName(Data.USERNAME);


                    //load the Xml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineScene.fxml"));
                    //initalizing controller with the socket
                    OnlineSceneController controller = new OnlineSceneController(socket);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    //gitting the current state
                    Stage window = (Stage) root.getScene().getWindow();
                    window.setTitle(Data.USERNAME);
                    //making the height and the width fixed
                    window.setMaxHeight(530);
                    window.setMaxWidth(700);
                    window.setMinHeight(530);
                    window.setMinWidth(700);

                    /* closing the socket on closing the window
                     * will cause an exception in ClientThreat class
                     * which will terminate it
                     * if the socket remain open the threat will remain even after closing the program  */

                    window.setOnCloseRequest(e->{
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            System.out.println(e1);
                        }
                    });

                    //switching Scenes
                    window.setScene(new Scene(parent, 650, 500));

                }else {                                                                 //if the server send "0"

                    warning.setText("name is taken");

                }

            } catch (IOException e) {

                //server is not connected
                warning.setText("server not ok");
                System.out.println("server not ok");

            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Male.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Female.setSelected(false);
                male = Male.isSelected();
                female= Female.isSelected();
            }
        });

        Female.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Male.setSelected(false);
                male = Male.isSelected();
                female= Female.isSelected();
            }
        });
    }
}
