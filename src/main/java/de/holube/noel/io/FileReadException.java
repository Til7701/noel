package de.holube.noel.io;

import java.io.IOException;

public class FileReadException extends Exception {

    FileReadException(String message, IOException cause) {
        super(message, cause);
    }

}
