package de.holube.noel.fx.view;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SpecialEditorView extends CodeArea {

    protected static final String DEFAULT_TEXT_STYLE_CLASS = "noel-text";

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

    protected void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.setStyleSpans(0, highlighting);
    }

    public void close() {

    }

    protected abstract Pattern getPattern();

    protected abstract String[] getPatternGroups();

}
