package de.holube.demo;

import javafx.application.Preloader;
import javafx.stage.Stage;

public class MyPreloader extends Preloader {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("preloader is running");
    }

}
