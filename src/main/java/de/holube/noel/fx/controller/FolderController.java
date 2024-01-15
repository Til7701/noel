package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.FolderView;
import de.holube.noel.model.FolderModel;
import de.holube.noel.model.WorkspaceManager;
import javafx.scene.control.TreeItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FolderController {

    private final FolderView folderView;
    private final WorkspaceManager workspaceManager;

    void setFolderModel(TreeItem<FolderModel> folderModel) {
        folderModel.setExpanded(true);
        folderView.getFolderTree().setRoot(folderModel);
        folderView.getFolderTree().refresh();
        folderView.getFolderTree().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.getValue().isDirectory()) {
                workspaceManager.openFileFromFolderModel(newValue.getValue());
            }
        });
    }

}
