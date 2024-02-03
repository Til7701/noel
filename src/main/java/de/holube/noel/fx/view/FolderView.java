package de.holube.noel.fx.view;

import de.holube.noel.model.FolderModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class FolderView extends AnchorPane {

    private final TreeView<FolderModel> folderTree = new TreeView<>();

    public FolderView() {
        folderTree.setCellFactory(param -> new FolderCell());
        AnchorPane.setTopAnchor(folderTree, 0D);
        AnchorPane.setLeftAnchor(folderTree, 0D);
        AnchorPane.setRightAnchor(folderTree, 0D);
        AnchorPane.setBottomAnchor(folderTree, 0D);
        this.getChildren().add(folderTree);
    }

    private static class FolderCell extends TreeCell<FolderModel> {
        @Override
        protected void updateItem(FolderModel folderModel, boolean empty) {
            super.updateItem(folderModel, empty);
            if (empty || folderModel == null) {
                this.setText("");
            } else {
                this.setText(folderModel.getName());
            }
        }
    }

}
