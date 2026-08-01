package moddedmite.modernmite.internal.chat;

public final class ChatTextSupport {

    public static final char FORMAT_CODE = (char) 0x00A7;

    public static int rawIndex(String text, int plainIndex) {
        int rawIndex = 0;
        int visibleCharacters = 0;
        while (rawIndex < text.length() && visibleCharacters < plainIndex) {
            if (text.charAt(rawIndex) == FORMAT_CODE && rawIndex + 1 < text.length()) {
                rawIndex += 2;
            } else {
                rawIndex++;
                visibleCharacters++;
            }
        }
        return rawIndex;
    }

    public static int leadingFormattingLength(String text) {
        int index = 0;
        while (index + 1 < text.length() && text.charAt(index) == FORMAT_CODE) {
            index += 2;
        }
        return index;
    }
}
