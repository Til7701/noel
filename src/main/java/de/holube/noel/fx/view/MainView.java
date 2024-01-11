package de.holube.noel.fx.view;

import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class MainView extends Pane {

    private final EditorView editorView;

    public MainView(EditorView editorView) {
        this.editorView = editorView;
        getChildren().add(editorView);
    }

}
