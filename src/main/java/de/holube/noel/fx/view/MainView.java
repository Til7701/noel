package de.holube.noel.fx.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class MainView extends Pane {

    private final TextArea textArea = new TextArea();

    public MainView() {
        getChildren().add(textArea);
    }

}
