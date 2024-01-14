package de.holube.noel.io;

import de.holube.noel.model.FileModel;
import de.holube.noel.model.FolderModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
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

    public void getParentDirectoryPath(String path, Consumer<String> successConsumer) {
        executorService.submit(() -> {
            log.info("Getting parent directory for file: " + path);
            try {
                File file = new File(path);
                if (!file.exists())
                    throw new FileNotFoundException();
                if (file.isDirectory()) {
                    log.debug("directory path: " + path);
                    successConsumer.accept(path);
                } else if (file.getParent() != null) {
                    String dirPath = file.getParent();
                    log.debug("directory path: " + dirPath);
                    successConsumer.accept(dirPath);
                } else {
                    String absolutePath = file.getAbsolutePath();
                    String[] splitPath = absolutePath.split(File.separator.equals("\\") ? "\\\\" : File.separator);
                    String[] splitDirPath = new String[splitPath.length - 1];
                    System.arraycopy(splitPath, 0, splitDirPath, 0, splitPath.length - 1);
                    String dirPath = String.join(File.separator, splitDirPath);
                    log.debug("directory path: " + dirPath);
                    successConsumer.accept(dirPath);
                }
            } catch (FileNotFoundException e) {
                log.error("Could not get parent directory", e);
            }
        });
    }

    public void createFolderModel(String path, Consumer<FolderModel> successConsumer, Consumer<FolderModelException> failConsumer) {
        executorService.submit(() -> {
            log.info("Creating folder model for: " + path);
            try {
                File root = new File(path);
                FolderModel folderModel = new FolderModel(root.getPath(), root.getName(), root.isDirectory());
                createFolderModel(new File(path), folderModel);
                successConsumer.accept(folderModel);
            } catch (IOException e) {
                log.error("Could not create folder model", e);
                failConsumer.accept(new FolderModelException("Could not create folder model", e));
            }
        });
    }

    private void createFolderModel(File file, FolderModel parent) throws IOException {
        FolderModel folderModel = new FolderModel(file.getPath(), file.getName(), true);
        if (file.isDirectory()) {
            folderModel.setDirectory(true);
            parent.getChildren().add(folderModel);
            for (File f : Objects.requireNonNull(file.listFiles())) {
                createFolderModel(f, folderModel);
            }
        } else {
            parent.getChildren().add(folderModel);
        }
    }

    public void close() {
        executorService.shutdown();
    }

}
