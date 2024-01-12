package de.holube.noel.model;

import de.holube.noel.fx.controller.MainController;
import de.holube.noel.io.AsyncFileIO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class FileManager {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    private final AsyncFileIO fileIO;

    private final Map<String, FileModel> openFiles = new HashMap<>();

    @Setter
    private MainController mainController;

    public FileManager(AsyncFileIO fileIO) {
        this.fileIO = fileIO;
    }

    public void addFile(FileModel fileModel) {
        writeLock.lock();
        try {
            openFiles.put(fileModel.getPath(), fileModel);
            mainController.setFile(fileModel);
        } finally {
            writeLock.unlock();
        }
    }

    public void saveFile(FileModel fileModel) {
        writeLock.lock();
        try {
            openFiles.put(fileModel.getPath(), fileModel);
        } finally {
            writeLock.unlock();
        }
        fileIO.saveFile(fileModel);
    }

}
