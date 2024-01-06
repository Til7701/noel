package de.holube.noel.io;

import java.io.IOException;

public class FileSaveException extends Exception {

    FileSaveException(String message, IOException cause) {
        super(message, cause);
    }

}
