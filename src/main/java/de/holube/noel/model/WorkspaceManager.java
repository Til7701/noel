package de.holube.noel.model;

import de.holube.noel.fx.controller.MainController;
import de.holube.noel.io.AsyncFileIO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class WorkspaceManager {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    private final AsyncFileIO fileIO;

    private final Map<String, FileModel> openFiles = new HashMap<>();

    @Setter
    private MainController mainController;

    public WorkspaceManager(AsyncFileIO fileIO) {
        this.fileIO = fileIO;
    }

    public void openFile(FileModel fileModel) {
        log.debug("Opening file: {}", fileModel.getPath());
        writeLock.lock();
        try {
            openFiles.put(fileModel.getPath(), fileModel);
            mainController.setFile(fileModel);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean saveFiles() {
        log.debug("Saving all files");
        readLock.lock();
        final CountDownLatch latch = new CountDownLatch(openFiles.size());
        final AtomicBoolean failure = new AtomicBoolean(false);
        try {
            for (var entry : openFiles.entrySet()) {
                fileIO.saveFile(entry.getValue(), latch::countDown, e -> {
                    failure.set(true);
                    latch.countDown();
                });
            }
            latch.await();
            return !failure.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            readLock.unlock();
        }
    }

    public void closeAllFiles() {
        log.debug("Closing all files");
        writeLock.lock();
        try {
            openFiles.clear();
            mainController.closeAllFiles();
        } finally {
            writeLock.unlock();
        }
    }

    public void closeFile(FileModel fileModel) {
        log.debug("Closing file: {}", fileModel.getPath());
        writeLock.lock();
        try {
            openFiles.remove(fileModel.getPath(), fileModel);
            mainController.closeFile(fileModel);
        } finally {
            writeLock.unlock();
        }
    }

    public void setFolderModel(FolderModel folderModel) {
        writeLock.lock();
        try {
            mainController.setFolderModel(folderModel);
        } finally {
            writeLock.unlock();
        }
    }

    public void openFileFromFolderModel(FolderModel folderModel) {
        mainController.syncModels();
        if (saveFiles()) {
            closeAllFiles();
            fileIO.loadFile(folderModel.getPath(), this::openFile);
        } else
            log.error("did not open file, could not save previous one");
    }

}
