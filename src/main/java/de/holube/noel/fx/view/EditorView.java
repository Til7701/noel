package de.holube.noel.fx.view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
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
        codeArea.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        this.getChildren().add(codeArea);
    }

}
