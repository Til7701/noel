package de.holube.noel.io;

import java.io.IOException;

public class FolderModelException extends Exception {

    FolderModelException(String message, IOException cause) {
        super(message, cause);
    }

}
