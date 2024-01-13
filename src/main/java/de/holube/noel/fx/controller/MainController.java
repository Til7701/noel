package de.holube.noel.fx.controller;

import de.holube.noel.fx.StageManager;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.model.FileManager;
import de.holube.noel.model.FileModel;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainController {

    private final MainView mainView;

    private final StageManager stageManager;
    private final FileManager fileManager;

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
            fileManager.saveFiles(); // TODO handle fail
        });
    }

    public void closeFile(FileModel fileModel) {
        Platform.runLater(() -> {
            stageManager.setTitle("NoEl");
            editorController.closeFile(fileModel);
        });
    }

}
