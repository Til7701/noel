package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.FolderView;
import de.holube.noel.model.FolderModel;
import javafx.scene.control.TreeItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FolderController {

    private final FolderView folderView;

    void setFolderModel(TreeItem<FolderModel> folderModel) {
        folderModel.setExpanded(true);
        folderView.getFolderTree().setRoot(folderModel);
        folderView.getFolderTree().refresh();
    }

}
