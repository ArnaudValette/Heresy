package com.arva.heresy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

class Marker{
    Byte type;
    int start;
    public Marker(Byte t, int s){
        type = t;
        start= s;

    }

    public void describe() {
        System.out.println("");
        System.out.println("_____");
        System.out.println(type);
        System.out.println(start);
        System.out.println("_____");
    }
}

public class FormatParser {

    FormatParserConfig config;

    public FormatParser(FormatParserConfig c){
        config = c;
    }
    public FormatResult parse(String s, List<Integer> limits) {
    // we consider the string we are given is already a substring of a line
    // the limits are there for a future need ( when we want to order results e.g.)
        int startOffset = limits.get(0);
        FormatNodes formats = new FormatNodes();
        FormatParserState state = new FormatParserState();
        generateMarkers(s, state);
        generateFormats(s, state, formats, startOffset);
        return new FormatResult(formats, limits);
    }

    public void generateFormats(String s, FormatParserState state, FormatNodes formats, int offset){
        state.initFlags();
        for(int i = 0, j = state.markers.size() ; i<j-1; i=i+1){
            Marker m1 = state.markers.get(i);
            Marker m2 = state.markers.get(i + 1);
            int start = m1.start+1;
            int end = m2.start;
            String content = s.substring(start,end);
            state.commitFlag(m1.type);
            formats.push(start + offset, end + offset, state.type, content);
        }
        if (!formats.has(0)) {
            // there are no formats nodes
            formats.push(0+offset, s.length()+offset, 0b000000, s);
        }
        else {
            if (formats.get(0).start > 1+offset) {
                // some non formatted text should be pushed at the start
                int start = 0 + offset;
                int end = formats.get(0).start;
                String content = s.substring(0, end - offset);
                formats.push(start, end, 0b000000, content, 0);
            }
            if (formats.getLast().end < s.length()+offset) {
                // some non formatted text should be push at the end
                int start = formats.getLast().end;
                int end = s.length() + offset;
                String content = s.substring(start - offset, s.length());
                formats.push(start, end, 0b000000, content);
            }
        }
    }

    public void generateMarkers(String s, FormatParserState state){
        for(int i = 0, j = s.length(); i<j; i = i+1){
            char c = s.charAt(i);
            boolean isInner = i < j - 1 && i > 0;
            if(config.has(c)){
                Byte currentFlag = config.get(c);
                if(state.hasFlag(currentFlag)){
                    boolean cond = (isInner && isValidSituation(s.charAt(i + 1), s.charAt(i - 1), c));
                    if (cond || !isInner) {
                            state.handleMarker(currentFlag, i);
                    }
                }

                else{
                    boolean cond = (isInner && isValidSituation(s.charAt(i - 1), s.charAt(i + 1), c));
                    if (cond || !isInner) {
                            state.pushWorkStack(new Marker(currentFlag, i));
                            state.commitFlag(currentFlag);
                    }
                }
            }
        }
        if (state.markers.size() > 1) {
            state.markers.sort((a, b) -> Integer.compare(a.start, b.start));
        }
    }

    public boolean isValidSituation(char a, char b, char c){
        return (a == ' ' || config.has(a)) && (b != ' ' && b != c);
    }

    private class FormatParserState {
        Map<Byte,Marker> workStack = new HashMap<>();
        List<Marker> markers = new ArrayList<>();
        Byte type = (byte) 0b000000;

        public void handleMarker(Byte flag, int i){
            Marker marker = getFromStack(flag);
            Marker closing = new Marker(flag, i);
            pushMarkers(marker);
            pushMarkers(closing);
            spliceWorkStack(flag);
            commitFlag(flag);
        }
        public boolean hasFlag(Byte flag){
            return (flag & type) == flag;
        }
        public void commitFlag(Byte flag){
            if (type == 0) {
                type = (byte) (flag | type);
            }
            else {
                type = (byte) (flag ^ type);
            }
        }
        public void initFlags(){
            this.type = (byte) 0b000000;
        }

        public void pushWorkStack(Marker m){
            workStack.put(m.type, m);
        }

        public Marker getFromStack(Byte t) {
            return workStack.get(t);
        }

        public void spliceWorkStack(Byte t) {
            workStack.remove(t);
        }

        public void pushMarkers(Marker m) {
            markers.add(m);
        }

    }
}
