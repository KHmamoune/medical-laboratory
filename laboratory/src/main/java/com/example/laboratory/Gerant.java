package com.example.laboratory;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class Gerant implements Initializable {
    @FXML
    private Button addUser;
    @FXML
    private ListView<String> userListView;
    private Client client;
    @FXML
    private VBox gerantUI;
    @FXML
    private VBox vboxUsers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client = ClientManager.getSharedClient();

        getAllUsers();



    }



    public void getAllUsers() {
        client.sendMessageToServer("getAllUsers: ");
        String messageFromServer = client.readMessageFromServer();
        System.out.println(messageFromServer);
        String[] usersData = messageFromServer.split("\n");
        String[] usersData2 =new String[usersData.length];

//        addUserToVbox(usersData);



        // Clear existing items in the ListView
        userListView.getItems().clear();


        // Populate the ListView with users
        userListView.getItems().addAll(Arrays.asList(usersData));

    }

    /*
    public void addUserToVbox(String [] userData){
        for(String user:userData) {
            String parts[]=user.split(":",5);
            String nom=parts[0];
            String prenom=parts[1];
            String username=parts[2];
            String password=parts[3];
            String type=parts[4];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    GridPane gridPane= new GridPane();
                    gridPane.prefWidthProperty().bind(vboxUsers.widthProperty());

                    gridPane.setPadding(new Insets(0,0,0,10));
                    gridPane.addRow(0,new Label(nom),new Label(prenom), new Label(username),new Label(password), new Label(type),new Label(),new Label());
                    vboxUsers.getChildren().add(gridPane);

                }
            });
        }

    }
    */


    @FXML
    public void showAddClientDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gerantUI.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addClientDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(fxmlLoader.load());
            ButtonType addButtonType = new ButtonType("Add Client", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButtonType) {
                AddClientController controller = fxmlLoader.getController();
                String message = "addClient:" + controller.getUserInfo();
                client.sendMessageToServer(message);
                String messageFromServer = client.readMessageFromServer();
                System.out.println(messageFromServer);
                getAllUsers();
            } else {
                System.out.println("cancel pressed");
            }

        } catch (IOException e) {
            System.out.println("couldn't load the AddClientDialog");
        }

    }
}

