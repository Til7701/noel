package de.holube.noel.fx.view;

import javafx.scene.layout.Pane;
import lombok.Getter;
import org.fxmisc.richtext.CodeArea;

@Getter
public class MainView extends Pane {

    private final CodeArea codeArea = new CodeArea();

    public MainView() {
        getChildren().add(codeArea);
    }

}
