package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.EditorView;
import de.holube.noel.model.FileModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditorController {

    private final EditorView editorView;

    public void setFile(FileModel fileModel) {
        editorView.getCodeArea().replaceText(fileModel.getContent());
    }

}
