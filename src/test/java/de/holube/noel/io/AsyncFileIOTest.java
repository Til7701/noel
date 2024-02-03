package de.holube.noel.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class AsyncFileIOTest {

    private static final String FILE_SEPARATOR_REGEX;

    static {
        if (File.separator.equals("\\"))
            FILE_SEPARATOR_REGEX = "\\\\";
        else
            FILE_SEPARATOR_REGEX = File.separator;
    }

    private AsyncFileIO fileIO;

    static void checkAbsolutePath(String path) {
        String startRegex;
        if (File.separator.equals("\\"))
            startRegex = "[A-Z]:" + FILE_SEPARATOR_REGEX;
        else
            startRegex = File.separator;
        assertTrue(path.startsWith(startRegex));
    }

    @BeforeEach
    void init() {
        fileIO = new AsyncFileIO();
    }

    //###################################################
    // getParentDirectoryPath(String, Consumer<String>)
    //###################################################

    @Test
    void dotTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>(null);
        Consumer<String> c = (s) -> {
            result.set(s);
            latch.countDown();
        };

        fileIO.getParentDirectoryPath(".", c);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        String path = result.get();

        assertNotNull(path);
        checkAbsolutePath(path);
        assertFalse(path.endsWith("."));
    }

    @Test
    void isFileTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>(null);
        Consumer<String> c = (s) -> {
            result.set(s);
            latch.countDown();
        };
        String fileName = "README.md";

        fileIO.getParentDirectoryPath(fileName, c);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        String path = result.get();

        assertNotNull(path);
        checkAbsolutePath(path);
        assertFalse(path.endsWith(fileName));
        assertTrue(path.endsWith("NoEl"));
    }

    @Test
    void isFileWithDotTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>(null);
        Consumer<String> c = (s) -> {
            result.set(s);
            latch.countDown();
        };
        String fileName = "./README.md";

        fileIO.getParentDirectoryPath(fileName, c);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        String path = result.get();

        assertNotNull(path);
        checkAbsolutePath(path);
        assertTrue(path.endsWith("NoEl"));
    }

}
