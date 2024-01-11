package de.holube.noel.fx;

import de.holube.noel.fx.controller.EditorController;
import de.holube.noel.fx.controller.MainController;
import de.holube.noel.fx.view.EditorView;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.model.FileManager;
import lombok.Getter;

@Getter
public class ViewControllerInitializer {

    private final MainView mainView;
    private final MainController mainController;

    private final EditorView editorView;
    private final EditorController editorController;

    public ViewControllerInitializer(StageManager stageManager, FileManager fileManager) {
        editorView = new EditorView();
        editorController = new EditorController(editorView);

        mainView = new MainView(editorView);
        mainController = new MainController(mainView, stageManager, fileManager, editorController);
    }

}
