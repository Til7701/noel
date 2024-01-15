package de.holube.noel.fx.controller;

import de.holube.noel.fx.StageManager;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.model.FileModel;
import de.holube.noel.model.FolderModel;
import de.holube.noel.model.WorkspaceManager;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainController {

    private final MainView mainView;

    private final StageManager stageManager;
    private final WorkspaceManager workspaceManager;

    private final FolderController folderController;
    private final EditorController editorController;

    public void setFile(FileModel fileModel) {
        Platform.runLater(() -> {
            stageManager.setTitle("NoEl - " + fileModel.getPath());
            editorController.setFileModel(fileModel);
        });
    }

    public void saveFiles() {
        Platform.runLater(() -> {
            editorController.updateFileModel();
            workspaceManager.saveFiles(); // TODO handle fail
        });
    }

    public void closeFile(FileModel fileModel) {
        Platform.runLater(() -> {
            stageManager.setTitle("NoEl");
            editorController.closeFile(fileModel);
        });
    }

    public void setFolderModel(FolderModel folderModel) {
        TreeItem<FolderModel> root = folderModel.createTreeItem();
        Platform.runLater(() ->
                folderController.setFolderModel(root)
        );
    }

}
