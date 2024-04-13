package de.holube.noel.fx.controller;

import de.holube.noel.fx.view.*;
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
        if (this.fileModel == null || !this.fileModel.getFileType().equals(fileModel.getFileType())) {
            editorView.getCodeArea().close();
            SpecialEditorView newCodeArea = switch (fileModel.getFileType()) {
                case MARKDOWN -> new MarkdownEditorView();
                case JAVA -> new JavaEditorView();
                case OTHER -> new DefaultEditorView();
            };
            editorView.setCodeArea(newCodeArea);
        } else {
            editorView.getCodeArea().clear();
        }
        editorView.getCodeArea().replaceText(fileModel.getContent());
        this.fileModel = fileModel;
    }

    void closeFile() {
        this.fileModel = null;
        editorView.getCodeArea().replaceText("");
    }
}
