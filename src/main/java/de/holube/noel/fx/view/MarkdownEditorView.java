package de.holube.noel.fx.view;

import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MarkdownEditorView extends SpecialEditorView {

    private static final String H1_PATTERN = "^# .+$";
    private static final String BOLD_PATTERN = "\\*\\*.+\\*\\*";
    private static final String ITALIC_PATTERN = "\\*.+\\*";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<BOLD>" + BOLD_PATTERN + ")" +
                    "|(?<ITALIC>" + ITALIC_PATTERN + ")"
    );

    private static final String[] PATTERN_GROUPS = {
            "BOLD", "ITALIC"
    };

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> Thread.ofVirtual().unstarted(r));
    private final Subscription cleanupWhenDone;

    public MarkdownEditorView() {
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

        this.getStylesheets().add(Objects.requireNonNull(EditorView.class.getResource("/css/markdown.css")).toExternalForm());
        this.setWrapText(true);
    }

    @Override
    protected StyleSpans<Collection<String>> computeHighlightingSpans(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = getPattern().matcher(text);
        int lastKwEnd = 0;
        while (matcher.find()) {
            String styleClass = getStyleClassForGroup(matcher);
            spansBuilder.add(Collections.singleton(DEFAULT_TEXT_STYLE_CLASS), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.singleton(DEFAULT_TEXT_STYLE_CLASS), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private String getStyleClassForGroup(Matcher matcher) {
        for (String group : getPatternGroups()) {
            if (matcher.group(group) != null) {
                return group.toLowerCase();
            }
        }
        return "";
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
    protected void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
        int currentParagraph = this.getCurrentParagraph();
        if (this.getParagraph(currentParagraph).getText().matches(H1_PATTERN)) {
            System.out.println(this.getParagraph(currentParagraph).getText());
            this.setParagraphStyle(currentParagraph, Collections.singleton("headingone"));
        }
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
