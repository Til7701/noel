package de.holube.noel.fx.view;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class EditorView extends AnchorPane {

    private SpecialEditorView codeArea = new DefaultEditorView();

    public EditorView() {
        setCodeArea(codeArea);
    }

    public void setCodeArea(SpecialEditorView codeArea) {
        this.getChildren().clear();
        AnchorPane.setTopAnchor(codeArea, 0D);
        AnchorPane.setLeftAnchor(codeArea, 0D);
        AnchorPane.setRightAnchor(codeArea, 0D);
        AnchorPane.setBottomAnchor(codeArea, 0D);
        this.codeArea = codeArea;
        this.getChildren().add(codeArea);
    }

    public void close() {
        codeArea.close();
    }

}
