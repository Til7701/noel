package de.holube.noel.model;

public enum FileType {
    MARKDOWN,
    JAVA,
    OTHER;

    public static FileType of(String name) {
        final String[] split = name.split("\\.");
        final String ending = split[split.length - 1].toLowerCase();
        return switch (ending) {
            case "md" -> FileType.MARKDOWN;
            case "java" -> FileType.JAVA;
            default -> FileType.OTHER;
        };
    }

}
