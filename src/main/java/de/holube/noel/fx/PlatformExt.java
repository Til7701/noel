package de.holube.noel.fx;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class PlatformExt {

    public static void runAndWait(final Runnable r) {
        if (Platform.isFxApplicationThread()) {
            try {
                r.run();
            } catch (Throwable t) {
                log.error("Error in runnable", t);
            }
        } else {
            final CountDownLatch doneLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    r.run();
                } finally {
                    doneLatch.countDown();
                }
            });
            try {
                doneLatch.await();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.error("Interrupted", ex);
            }
        }
    }

}
