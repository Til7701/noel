package de.holube.noel.fx.view;

import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

@Getter
public class FolderView extends AnchorPane {

    private final TreeView<String> folderTree = new TreeView<>();

    public FolderView() {
        AnchorPane.setTopAnchor(folderTree, 0D);
        AnchorPane.setLeftAnchor(folderTree, 0D);
        AnchorPane.setRightAnchor(folderTree, 0D);
        AnchorPane.setBottomAnchor(folderTree, 0D);
        this.getChildren().add(folderTree);
    }

}
