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
        FormatNodes formats = new FormatNodes();
        FormatParserState state = new FormatParserState();
        generateMarkers(s, state);

        state.markers.forEach(m->m.describe());
        return new FormatResult(formats, limits);
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

        public boolean paired(Byte flag){
            Byte a = (byte) (flag ^ type);
            Byte flagIsAbsent = (byte) (flag & a);
            return hasFlag(flag) ? (byte) flagIsAbsent == 0 : false;
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
