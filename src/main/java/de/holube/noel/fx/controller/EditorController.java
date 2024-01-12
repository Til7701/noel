package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.EditorView;
import de.holube.noel.model.FileModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditorController {

    private final EditorView editorView;

    private FileModel fileModel;

    public FileModel getFileModel() {
        fileModel.setContent(editorView.getCodeArea().getText());
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        editorView.getCodeArea().replaceText(fileModel.getContent());
        this.fileModel = fileModel;
    }

}
