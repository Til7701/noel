package de.holube.noel.fx.view;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;

@Getter
public class MainView extends AnchorPane {

    private final EditorView editorView;

    public MainView(EditorView editorView) {
        this.editorView = editorView;

        AnchorPane.setTopAnchor(editorView, 0D);
        AnchorPane.setLeftAnchor(editorView, 0D);
        AnchorPane.setRightAnchor(editorView, 0D);
        AnchorPane.setBottomAnchor(editorView, 0D);
        getChildren().add(editorView);
    }

}
