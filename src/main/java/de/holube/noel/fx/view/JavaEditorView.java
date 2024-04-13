package de.holube.noel.fx.view;

import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;


@Slf4j
public class JavaEditorView extends SpecialEditorView {

    private static final String[] KEYWORDS = new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")" +
                    "|(?<STRING>" + STRING_PATTERN + ")" +
                    "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String[] PATTERN_GROUPS = {
            "KEYWORD", "STRING", "COMMENT"
    };

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> Thread.ofVirtual().unstarted(r));
    private final Subscription cleanupWhenDone;

    public JavaEditorView() {
        // https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsAsyncDemo.java
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        cleanupWhenDone = this.multiPlainChanges()
                .successionEnds(Duration.ofMillis(200))
                .retainLatestUntilLater(executor)
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(this.multiPlainChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        log.error("Could not do things!", t.getFailure());
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        this.getStylesheets().add(Objects.requireNonNull(EditorView.class.getResource("/css/java.css")).toExternalForm());
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeHighlightingSpans(text);
            }
        };
        executor.execute(task);
        return task;
    }

    @Override
    public void close() {
        cleanupWhenDone.unsubscribe();
    }

    @Override
    protected Pattern getPattern() {
        return PATTERN;
    }

    @Override
    protected String[] getPatternGroups() {
        return PATTERN_GROUPS;
    }

}
