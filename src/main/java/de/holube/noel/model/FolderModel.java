package de.holube.noel.model;

import javafx.scene.control.TreeItem;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class FolderModel implements Comparable<FolderModel> {

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

    @Override
    public int compareTo(FolderModel o) {
        if (this.isDirectory() && !o.isDirectory())
            return -1;
        else if (!this.isDirectory() && o.isDirectory())
            return 1;
        return this.getName().compareTo(o.getName());
    }

}
