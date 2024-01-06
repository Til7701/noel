package de.holube.noel.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileModel {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    /**
     * the path to the file
     */
    private final String path;

    /**
     * the content of the file. valid, if read is true
     */
    private String content;

    /**
     * if the file is read and the content is considered valid
     */
    private boolean read;

    /**
     * true, if the content has been changed, since the file has been read
     */
    private boolean changed = false;


    public FileModel(String path, String content) {
        writeLock.lock();
        this.path = path;
        this.content = content;
        this.read = true;
        writeLock.unlock();
    }

    public String getPath() {
        readLock.lock();
        try {
            return path;
        } finally {
            readLock.unlock();
        }
    }

    public String getContent() {
        readLock.lock();
        try {
            return content;
        } finally {
            readLock.unlock();
        }
    }

    public void setContent(String content) {
        writeLock.lock();
        this.content = content;
        this.changed = true;
        writeLock.unlock();
    }

    public boolean isRead() {
        readLock.lock();
        try {
            return read;
        } finally {
            readLock.unlock();
        }
    }

    public void setRead(boolean read) {
        writeLock.lock();
        this.read = read;
        writeLock.unlock();
    }

    public boolean isChanged() {
        readLock.lock();
        try {
            return changed;
        } finally {
            readLock.unlock();
        }
    }

}
