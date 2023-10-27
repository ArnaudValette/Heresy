package com.arva.heresy;

class FormatResult {
    FormatNodes formats;
    int lineNumber;

    public FormatResult(FormatNodes f, int i) {
        formats = f;
        lineNumber = i;
    }
}
public class FormatParser {
    public FormatResult parse(String s, int lineNumber) {
        FormatNodes formats = new FormatNodes();
        return new FormatResult(formats, lineNumber);
    }
    private class ParserState {
    }
}
