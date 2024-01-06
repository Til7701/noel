package de.holube.noel;

import de.holube.noel.fx.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Taskbar.Feature;
import java.util.Arrays;

public class NoelApplication extends Application {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setupIcons(primaryStage);
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 320, 240);
        primaryStage.setTitle("NoEl");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method loads the icon for this application from the resources and sets on the taskbar and application bar.
     *
     * @param primaryStage the stage to set the icon on
     */
    private void setupIcons(Stage primaryStage) {
        //Set icon on the application bar
        final Image appIcon = new Image(String.valueOf(getClass().getResource("/icons/icon.png")));
        primaryStage.getIcons().add(appIcon);

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            final Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                final java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/icons/icon.png"));
                taskbar.setIconImage(dockIcon);
            }
        }
    }

}
