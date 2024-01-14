package de.holube.noel.model;

import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FolderModel {

    private final List<FolderModel> children = new ArrayList<>();
    private String path;
    private String name;
    private boolean directory;

    public FolderModel(String path, String name, boolean directory) {
        this.path = path;
        this.name = name;
        this.directory = directory;
    }

    public TreeItem<FolderModel> createTreeItem() {
        TreeItem<FolderModel> root = new TreeItem<>(this);
        children.forEach(c -> root.getChildren().add(c.createTreeItem()));
        return root;
    }

}
