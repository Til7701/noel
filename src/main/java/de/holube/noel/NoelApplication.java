package de.holube.noel;

import de.holube.noel.fx.StageManager;
import de.holube.noel.fx.ViewControllerInitializer;
import de.holube.noel.fx.controller.MainController;
import de.holube.noel.fx.view.EditorView;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.io.AsyncFileIO;
import de.holube.noel.model.WorkspaceManager;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

@Slf4j
public class NoelApplication extends Application {

    private static final KeyCombination SAVE_SHORTCUT = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    private static final AsyncFileIO fileIO = new AsyncFileIO();
    private static final WorkspaceManager WORKSPACE_MANAGER = new WorkspaceManager(fileIO);
    private static final Semaphore startupLock = new Semaphore(0);

    private MainController mainController;
    private EditorView editorView;

    public static void main(String[] args) {
        log.debug("Started with args: {}", Arrays.toString(args));

        if (args.length > 0) {
            fileIO.loadFile(args[0], fileModel -> {
                startupLock.acquireUninterruptibly();
                WORKSPACE_MANAGER.openFile(fileModel);
            });
            fileIO.getParentDirectoryPath(args[0], s ->
                    fileIO.createFolderModel(s, WORKSPACE_MANAGER::setFolderModel, e -> log.error("Could not create folder model!", e))
            );
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CSSFX.start();
        final StageManager stageManager = new StageManager(primaryStage);

        final ViewControllerInitializer initializer = new ViewControllerInitializer(stageManager, WORKSPACE_MANAGER);
        final MainView mainView = initializer.getMainView();
        mainController = initializer.getMainController();
        WORKSPACE_MANAGER.setMainController(mainController);
        editorView = initializer.getEditorView();

        final Scene scene = new Scene(mainView, Screen.getPrimary().getBounds().getWidth() * 0.7D, Screen.getPrimary().getBounds().getHeight() * 0.7D);
        setupShortcuts(scene);

        stageManager.setScene(scene);
        stageManager.setTitle("NoEl");
        primaryStage.show();
        startupLock.release();
    }

    private void setupShortcuts(Scene scene) {
        scene.getAccelerators().put(SAVE_SHORTCUT, () -> {
            log.debug("Save shortcut used");
            mainController.saveFiles();
        });
    }

    @Override
    public void stop() {
        fileIO.close();
        editorView.close();
    }

}
