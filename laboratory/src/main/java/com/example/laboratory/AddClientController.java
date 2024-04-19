package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {
@FXML
private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private ChoiceBox type;

    public String getUserInfo(){
        String nom=this.nom.getText().trim();
        String prenom=this.prenom.getText().trim();
        String username=this.username.getText().trim();
        String password=this.password.getText().trim();
        String type=this.type.getValue().toString().trim();
return nom+":"+prenom+":"+username+":"+password+":"+type;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
