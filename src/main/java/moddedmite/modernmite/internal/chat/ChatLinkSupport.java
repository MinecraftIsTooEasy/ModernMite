package moddedmite.modernmite.internal.chat;

import net.minecraft.ChatMessageComponent;
import net.minecraft.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatLinkSupport {

    private static final Pattern PLAIN_URL = Pattern.compile(
            "(?i)(?<![-\\w.@])(?:(?:https?://)?(?:[-\\w]+\\.)+[a-z]{2,63})(?::\\d+)?(?:/\\S*)?"
    );

    @Nullable
    public static URI parseHttpUrl(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            String normalized = value.trim();
            if (!normalized.regionMatches(true, 0, "http://", 0, 7)
                    && !normalized.regionMatches(true, 0, "https://", 0, 8)) {
                normalized = "http://" + normalized;
            }
            URI uri = new URI(normalized);
            String scheme = uri.getScheme();
            if (scheme == null || uri.getHost() == null) {
                return null;
            }
            scheme = scheme.toLowerCase(Locale.ROOT);
            return "http".equals(scheme) || "https".equals(scheme) ? uri : null;
        } catch (URISyntaxException exception) {
            return null;
        }
    }

    public static List<ChatLinkSpan> findPlainTextLinks(String formattedText) {
        String text = stripFormatting(formattedText);
        Matcher matcher = PLAIN_URL.matcher(text);
        List<ChatLinkSpan> links = new ArrayList<>();
        while (matcher.find()) {
            int end = matcher.end();
            while (end > matcher.start() && isTrailingPunctuation(text.charAt(end - 1))) {
                end--;
            }
            if (end > matcher.start()) {
                links.add(new ChatLinkSpan(matcher.start(), end, text.substring(matcher.start(), end)));
            }
        }
        return links.isEmpty() ? Collections.emptyList() : List.copyOf(links);
    }

    private static boolean isTrailingPunctuation(char character) {
        return character == '.' || character == ',' || character == ';' || character == ':'
                || character == '!' || character == '?' || character == ')' || character == ']';
    }

    public static boolean hasOpenUrl(ChatMessageComponent component) {
        ChatLinkComponentAccess access = (ChatLinkComponentAccess) component;
        if (access.modernMite$getOpenUrl() != null) {
            return true;
        }
        List<?> children = access.modernMite$getChildren();
        if (children != null) {
            for (Object child : children) {
                if (child instanceof ChatMessageComponent childComponent && hasOpenUrl(childComponent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Deque<List<ChatLinkSpan>> createWrappedLineLinks(ChatMessageComponent component,
                                                                   List<?> wrappedLines) {
        String fullText = stripFormatting(component.toStringWithFormatting(true));
        List<ChatLinkSpan> ranges = new ArrayList<>();
        collectRanges(component, 0, ranges);

        Deque<List<ChatLinkSpan>> result = new ArrayDeque<>(wrappedLines.size());
        int searchFrom = 0;
        for (int lineIndex = 0; lineIndex < wrappedLines.size(); lineIndex++) {
            String lineText = stripFormatting((String) wrappedLines.get(lineIndex));
            int lineStart = fullText.indexOf(lineText, searchFrom);
            if (lineStart < 0) {
                lineStart = Math.min(searchFrom, fullText.length());
            }
            int lineEnd = Math.min(lineStart + lineText.length(), fullText.length());
            int storedLineOffset = lineIndex == 0 ? 0 : 1;
            List<ChatLinkSpan> lineLinks = new ArrayList<>();
            for (ChatLinkSpan range : ranges) {
                int start = Math.max(range.start(), lineStart);
                int end = Math.min(range.end(), lineEnd);
                if (start < end) {
                    lineLinks.add(new ChatLinkSpan(
                            start - lineStart + storedLineOffset,
                            end - lineStart + storedLineOffset,
                            range.url()
                    ));
                }
            }
            result.addLast(lineLinks.isEmpty() ? Collections.emptyList() : List.copyOf(lineLinks));
            searchFrom = lineEnd;
        }
        return result;
    }

    private static int collectRanges(ChatMessageComponent component, int offset, List<ChatLinkSpan> ranges) {
        String text = stripFormatting(component.toStringWithFormatting(true));
        String url = ((ChatLinkComponentAccess) component).modernMite$getOpenUrl();
        if (url != null) {
            if (!text.isEmpty()) {
                ranges.add(new ChatLinkSpan(offset, offset + text.length(), url));
            }
            return text.length();
        }

        ChatLinkComponentAccess access = (ChatLinkComponentAccess) component;
        List<?> children = access.modernMite$getChildren();
        if (children == null || children.isEmpty()) {
            return text.length();
        }

        if (access.modernMite$getText() == null && access.modernMite$getTranslationKey() == null) {
            int childOffset = offset;
            for (Object child : children) {
                if (child instanceof ChatMessageComponent childComponent) {
                    childOffset += collectRanges(childComponent, childOffset, ranges);
                }
            }
            return text.length();
        }

        int searchFrom = 0;
        for (Object child : children) {
            if (!(child instanceof ChatMessageComponent childComponent)) {
                continue;
            }
            String childText = stripFormatting(childComponent.toStringWithFormatting(true));
            int childStart = text.indexOf(childText, searchFrom);
            if (childStart >= 0) {
                if (hasOpenUrl(childComponent)) {
                    collectRanges(childComponent, offset + childStart, ranges);
                }
                searchFrom = childStart + childText.length();
            }
        }
        return text.length();
    }

    private static String stripFormatting(String text) {
        String stripped = StringUtils.stripControlCodes(text);
        return stripped == null ? "" : stripped;
    }
}
