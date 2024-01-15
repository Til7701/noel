package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.EditorView;
import de.holube.noel.model.FileModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditorController {

    private final EditorView editorView;

    private FileModel fileModel;

    void updateFileModel() {
        fileModel.setContent(editorView.getCodeArea().getText());
    }

    void setFileModel(FileModel fileModel) {
        editorView.getCodeArea().replaceText(fileModel.getContent());
        this.fileModel = fileModel;
    }

    void closeFile() {
        this.fileModel = null;
        editorView.getCodeArea().replaceText("");
    }
}
