package de.holube.noel;

import de.holube.noel.fx.StageManager;
import de.holube.noel.fx.ViewControllerInitializer;
import de.holube.noel.fx.controller.MainController;
import de.holube.noel.fx.view.MainView;
import de.holube.noel.io.AsyncFileIO;
import de.holube.noel.model.FileManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

@Slf4j
public class NoelApplication extends Application {

    private static final KeyCombination SAVE_SHORTCUT = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    private static final AsyncFileIO fileIO = new AsyncFileIO();
    private static final FileManager fileManager = new FileManager(fileIO);
    private static final Semaphore startupLock = new Semaphore(0);

    private MainController mainController;

    public static void main(String[] args) {
        log.debug("Started with args: " + Arrays.toString(args));

        if (args.length > 0) {
            fileIO.loadFile(args[0], fileModel -> {
                startupLock.acquireUninterruptibly();
                fileManager.addFile(fileModel);
            });
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final StageManager stageManager = new StageManager(primaryStage);

        final ViewControllerInitializer initializer = new ViewControllerInitializer(stageManager, fileManager);
        final MainView mainView = initializer.getMainView();
        mainController = initializer.getMainController();
        fileManager.setMainController(mainController);

        final Scene scene = new Scene(mainView, 1024, 720);
        setupShortcuts(scene);

        stageManager.setScene(scene);
        stageManager.setTitle("NoEl");
        primaryStage.show();
        startupLock.release();
    }

    private void setupShortcuts(Scene scene) {
        scene.getAccelerators().put(SAVE_SHORTCUT, () -> {
            log.debug("Save shortcut used");
            mainController.saveCurrentFile();
        });
    }

    @Override
    public void stop() {
        fileIO.close();
    }

}
