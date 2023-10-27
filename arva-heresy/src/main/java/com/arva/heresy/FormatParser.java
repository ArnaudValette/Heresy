package com.arva.heresy;

import java.util.List;

class FormatResult {
    FormatNodes formats;
    int start;
    int end;

    public FormatResult(FormatNodes f, List<Integer> limits) {
        formats = f;
        start = limits.get(0);
        end = limits.get(1);
    }
}
public class FormatParser {
    public FormatResult parse(String s, List<Integer> limits) {
        FormatNodes formats = new FormatNodes();
        return new FormatResult(formats, limits);
    }
    private class ParserState {
    }
}
