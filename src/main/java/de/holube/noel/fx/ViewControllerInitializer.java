package de.holube.noel.fx;

import de.holube.noel.fx.controller.EditorController;
import de.holube.noel.fx.controller.FolderController;
import de.holube.noel.fx.controller.MainController;
import de.holube.noel.fx.view.EditorView;
import de.holube.noel.fx.view.FolderView;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.model.WorkspaceManager;
import lombok.Getter;

@Getter
public class ViewControllerInitializer {

    private final MainView mainView;
    private final MainController mainController;

    private final FolderView folderView;
    private final FolderController folderController;

    private final EditorView editorView;
    private final EditorController editorController;

    public ViewControllerInitializer(StageManager stageManager, WorkspaceManager workspaceManager) {
        folderView = new FolderView();
        folderController = new FolderController(folderView, workspaceManager);

        editorView = new EditorView();
        editorController = new EditorController(editorView);

        mainView = new MainView(folderView, editorView);
        mainController = new MainController(mainView, stageManager, workspaceManager, folderController, editorController);
    }

}
