package de.holube.noel.fx.view;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import org.fxmisc.richtext.CodeArea;

@Getter
public class EditorView extends AnchorPane {

    private final CodeArea codeArea = new CodeArea();

    public EditorView() {
        AnchorPane.setTopAnchor(codeArea, 0D);
        AnchorPane.setLeftAnchor(codeArea, 0D);
        AnchorPane.setRightAnchor(codeArea, 0D);
        AnchorPane.setBottomAnchor(codeArea, 0D);
        this.getChildren().add(codeArea);
    }

}
