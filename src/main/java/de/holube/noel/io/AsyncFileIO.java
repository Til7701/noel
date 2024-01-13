package de.holube.noel.io;

import de.holube.noel.model.FileModel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class AsyncFileIO {

    private static final AtomicInteger threadCount = new AtomicInteger(0);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("FileIOThread-" + threadCount.getAndIncrement());
        thread.setDaemon(false);
        return thread;
    });

    public void loadFile(final String path, Consumer<FileModel> successConsumer) {
        loadFile(path, successConsumer, e -> log.error("Could not load file", e));
    }

    public void loadFile(final String path, Consumer<FileModel> successConsumer, Consumer<FileReadException> failConsumer) {
        executorService.submit(() -> {
            log.info("Loading file: " + path);
            try {
                String content = Files.readString(Path.of(path));
                log.debug("Loaded file: " + path + "successfully");
                successConsumer.accept(new FileModel(path, content));
            } catch (IOException e) {
                failConsumer.accept(new FileReadException("Could not read file", e));
            }
        });
    }

    public void saveFile(FileModel fileModel) {
        saveFile(fileModel, () -> log.debug("Saved file"), e -> log.error("Could not save file", e));
    }

    public void saveFile(FileModel fileModel, Consumer<FileSaveException> failConsumer) {
        saveFile(fileModel, () -> log.debug("Saved file"), failConsumer);
    }

    public void saveFile(FileModel fileModel, Runnable successConsumer, Consumer<FileSaveException> failConsumer) {
        executorService.submit(() -> {
            log.info("Saving file: " + fileModel.getPath());
            try {
                Files.writeString(Path.of(fileModel.getPath()), fileModel.getContent());
                log.debug("Saved file: " + fileModel.getPath() + " successfully");
                successConsumer.run();
            } catch (IOException e) {
                log.error("Could not save file", e);
                failConsumer.accept(new FileSaveException("Could not save file", e));
            }
        });
    }

    public void close() {
        executorService.shutdown();
    }

}
