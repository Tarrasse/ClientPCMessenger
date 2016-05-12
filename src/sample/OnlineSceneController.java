package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by mahmoud on 4/30/2016.
 */
public class OnlineSceneController implements Initializable {

    //GUI elements
    public  ListView<String> onlineListView ;
    public Button ChatButton;
    public Label warnMsg;
    public VBox root;
    public Label friendNameLable ;
    public TextArea chatTextArea ;
    public TextField msgTextField;
    public Button sendButton;

    //private data
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private String[] CurrentUsers;
    private String selsectedUser;

    public OnlineSceneController(Socket socket) {

        //initializing the socket
        this.socket = socket;

        try {
            //initializing the PrintWriter
            output = new PrintWriter(socket.getOutputStream(),true);

            //requesting the online list
            output.println("#?!");

            //initializing the Scanner
            input = new Scanner(socket.getInputStream());

            //getting the online list
            String message =  input.nextLine();

            //checking
            if(message.contains("#?!")){

                //cut out the sign
                String temp = message.substring(3);
                temp=temp.replace("[","");
                temp=temp.replace("]","");

                //creting array of online users
                CurrentUsers = temp.split(", ");
            }else {
                //any other message
                System.out.println("new Message : " + message);
            }

        } catch (IOException e){

            System.out.println(e + " connection error");
        }

    }

    //on send button clicked
    public void send(){
        //getting the text enterd
        String message = msgTextField.getText().trim();
        if(message.equals("")){
            return;
        }
        //removing the text from TextField
        msgTextField.setText("");
        //appending the new line (new message)
        chatTextArea.setText(chatTextArea.getText() + "\n YOU: " +message);
        //sending the message to the server
        output.println(Data.USERNAME +": " +message);

    }


    //on clicking the reload image
    public void reload() {
        //send online users request
        output.println("#?!");
    }

    //automatically called on lunching the Scene
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //not empty
        if (CurrentUsers.length > 0 ){

            //if the first element not the empty string
            if(CurrentUsers[0].length() > 3 )
                onlineListView.getItems().addAll(CurrentUsers);

        }

        //indicate the user that his massages is Public
        friendNameLable.setText("public Chat room");

        //starting the ClientThread (get the messages automatically )
        new ClientThread().start();

    }

    public void start(ActionEvent actionEvent) throws IOException {
        /* if chat Button clicked without selecting any one that makes the user
        *  on the public chat room */

        if(onlineListView.getSelectionModel().getSelectedIndex() != -1){                    //someone selected

            //getting the selected user
            selsectedUser = onlineListView.getSelectionModel().getSelectedItem();
            selsectedUser=selsectedUser.trim();


            //if sending selected user request
            output.println("123!@#" + selsectedUser);
            friendNameLable.setText(selsectedUser);

        }else{                                                                              //no selection

            //sending Public request
            output.println("123!@####");
            friendNameLable.setText("PUBLIC chat room");        }
    }

    public class ClientThread extends Thread{
        /* this threat made to listen to the new messages automatically */

        //automatically called
        @Override
        public void run() {

            //the incoming messsage
            String message;
            try {
                while (true){
                    // listing to server
                    message = input.nextLine();                                     //waiting for the server to send a message
                    System.out.println(message);
                    if(message.contains("#?!")){                                    //special message (online users)

                        /* updates the list view */

                        String temp = message.substring(3);
                        temp=temp.replace("[","");
                        temp=temp.replace("]","");
                        CurrentUsers = temp.split(", ");
                        System.out.println("'" + CurrentUsers[0] +"'");
                        if(CurrentUsers[0].length() > 3){
                            onlineListView.getItems().setAll(CurrentUsers);
                        }

                    }else {

                        // normal message
                        chatTextArea.setText(chatTextArea.getText() + "\n" + message);

                    }
                }
            }catch (Exception e){
                /* when the socket is closed
                *  exception well be generated
                *  and this threat will be terminated automatically */
                System.out.println(e);
            }
        }

    }

}
