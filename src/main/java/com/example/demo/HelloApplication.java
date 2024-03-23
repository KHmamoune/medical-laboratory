package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Stage stage;
    private double initWidth = 800;
    private double initHeight = 500;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), this.initWidth, this.initHeight);
        this.stage.setTitle("Medical Laboratory");
        this.stage.setScene(scene);
        this.stage.show();

        Button button = (Button) scene.lookup("#login");
        Button button1 = (Button) scene.lookup("#login1");
        Button button2 = (Button) scene.lookup("#login2");
        Button button3 = (Button) scene.lookup("#login3");
        button.setOnAction(e -> handleClick());
        button1.setOnAction(e -> handleClick());
        button2.setOnAction(e -> handleClick());
        button3.setOnAction(e -> handleClick());
    }

    public void handleClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        try{
            this.stage.setScene(new Scene(fxmlLoader.load(), this.initWidth, this.initHeight));
        } catch (IOException e1){ }
    }

    public static void main(String[] args) {
        launch();
    }
}