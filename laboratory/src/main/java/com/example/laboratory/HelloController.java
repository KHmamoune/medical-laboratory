package com.example.laboratory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button login_btn;
    private Client client;
    private String type = "";




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // try {
        client =ClientManager.getSharedClient();


        client.setOnUserTypeReceived(userType -> {
            Platform.runLater(() -> {
                closeCurrentStage();
                openNewStage(userType);
            });
        });
        //  client.receiveMessageFromServer();

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userInfo = username.getText() + ":" + password.getText();
                client.sendMessageToServer(userInfo);
                handleAuthentication();
            }}

        );



        //  } catch (IOException e) {
        //    e.printStackTrace();
        //}

    }
    private void closeCurrentStage() {
        ((Stage) login_btn.getScene().getWindow()).close();
    }

    private void openNewStage(String userType) {
        try {
            String fxmlPath;
            if (userType.equals("gerant")) {
                fxmlPath = "Gestion-Personnel.fxml";

            }  else if(userType.equals("doctor")){
                fxmlPath = "Validation.fxml";

            } else if (userType.equals("agent")) {
                fxmlPath="Reception.fxml";

            } else if (userType.equals("technecien")) {
                fxmlPath="Plan.fxml";

            } else {
                fxmlPath = "User.fxml";

            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleAuthentication() {
            String messageFromServer= client.readMessageFromServer();
            if (messageFromServer.equals("Authentication failed")) {
                System.out.println("Authentication failed");
                // Handle authentication failure, show error message, etc.

            } else if (messageFromServer.equals("gerant")) {
                this.type = "gerant";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("gerant");

            } else if (messageFromServer.equals("agent")) {
                this.type = "agent";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("type");
                
            } else if (messageFromServer.equals("doctor")) {
                this.type = "doctor";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("doctor");

            } else if (messageFromServer.equals("technicien")) {
                this.type = "technicien";
                System.out.println(this.type);
                // Notify UI controller about user type
                closeCurrentStage();
                openNewStage("technicien");

            }

    }

}


