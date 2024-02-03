package de.holube.noel.fx.view;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MainView extends AnchorPane {

    private final EditorView editorView;
    private final FolderView folderView;

    private final SplitPane splitPane = new SplitPane();

    public MainView(FolderView folderView, EditorView editorView) {
        this.folderView = folderView;
        this.editorView = editorView;

        AnchorPane.setTopAnchor(splitPane, 0D);
        AnchorPane.setLeftAnchor(splitPane, 0D);
        AnchorPane.setRightAnchor(splitPane, 0D);
        AnchorPane.setBottomAnchor(splitPane, 0D);
        this.getChildren().add(splitPane);

        splitPane.getItems().addAll(folderView, editorView);
        splitPane.setDividerPosition(0, 0.2D);

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    }

}
