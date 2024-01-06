package de.holube.demo.fx.view;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class MainView extends Pane {

    public MainView() {
        getChildren().add(new Label("Hello World!"));
    }

}
