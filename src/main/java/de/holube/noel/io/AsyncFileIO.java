package de.holube.noel.io;

import de.holube.noel.model.FileModel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
public class AsyncFileIO {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void loadFile(final String path, Consumer<FileModel> successConsumer) {
        loadFile(path, successConsumer, e -> log.error("Could not load file", e));
    }

    public void loadFile(final String path, Consumer<FileModel> successConsumer, Consumer<FileReadException> failConsumer) {
        executorService.submit(() -> {
            log.info("Loading file: " + path);
            try {
                String content = Files.readString(Path.of(path));
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
                successConsumer.run();
            } catch (IOException e) {
                failConsumer.accept(new FileSaveException("Could not save file", e));
            }
        });
    }

    public void close() {
        executorService.shutdown();
    }

}
