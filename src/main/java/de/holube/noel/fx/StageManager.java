package de.holube.noel.fx;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class StageManager {

    private final Stage stage;

    public StageManager(Stage stage) {
        this.stage = stage;
        stage.setWidth(700);
        stage.setHeight(500);
        setupIcons();
    }

    /**
     * This method loads the icon for this application from the resources and sets on the taskbar and application bar.
     */
    private void setupIcons() {
        //Set icon on the application bar
        final Image appIcon = new Image(String.valueOf(getClass().getResource("/icons/icon.png")));
        stage.getIcons().add(appIcon);

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            final Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                final java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/icons/icon.png"));
                taskbar.setIconImage(dockIcon);
            }
        }
    }

    public void setTitle(String title) {
        Platform.runLater(() -> stage.setTitle(title));
    }

    public void setScene(Scene scene) {
        Platform.runLater(() -> stage.setScene(scene));
    }

}
