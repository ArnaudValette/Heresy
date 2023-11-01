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
    public FormatResult parse(String s,List<Integer> limits) {
    // we consider the string we are given is already a substring of a line
    // the limits are there for a future need ( when we want to order results e.g.)
    /*
      First, we run through the string and find pairs of markers that corresponds
      to a valid starting marker and valid ending marker
      Second, we run through these markers (sorted by order of appearance) and
      we compute, if necessary, composed types.
     */
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
            int start = m1.start;
            int end = m2.start;
            String content = s.substring(start + 1,end);
            state.commitFlag(m1.type);

            /*
              In order to merge composed markers, we have to count an offset
              to get the proper beginning of the composed type:
              e.g. +/strike-italic/+ does not begin or end at /, but at +
              the strike only node that lies between + and / is not interesting to us,
              we don't store a format for it and instead, we retain information on
              where is this strike only starting -> the next format of composed type +/
              needs to know this information to include the verbatim + .
              Note that the content field of a format is the inner text between markers,
              while the start and end is refering to the inner text PLUS the surrounding
              markers. (You need both).
             */

            if (content != "") {
                int preOff = formats.getPreCount();
                formats.push(start+offset-preOff, end + offset + 1 + preOff, state.type, content);
            }
            else{
                formats.pre();
            }
        }

        if (!formats.has(0)) {
            formats.push(0+offset, s.length()+offset, 0b000000, s);
        }
        else {
            if (formats.get(0).start > 1+offset) {
                int start = 0 + offset;
                int end = formats.get(0).start - 1;
                String content = s.substring(0, end - offset);
                formats.push(start, end, 0b000000, content, 0);
            }
            if (formats.getLast().end < s.length()+offset) {
                int start = formats.getLast().end;
                int end = s.length() + offset;
                String content = s.substring(start - offset + 1, s.length());
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
                // state.hasFlag 
                if(state.hasFlag(currentFlag)){
                    /*
                      A marker is valid when it is surrounded by a space and a char 
                      that is different than a space OR the same marker
                     */
                    boolean cond = isValidMarker(1, isInner, s, i, c);
                    if (cond || !isInner) {
                        /* this is an ending marker
                           the state removes the currentFlag from its
                           inner flag.
                           e.g. *bold* -> we are reaching the second star,
                           in that case, our current flag is 0b100000
                           the state flag could be anything, but because we
                           encountered the first star, it contains at least
                           0b100000. state.hasFlag(currentFlag) is a condition
                           to reach this code.
                         */
                            state.handleMarker(currentFlag, i);
                    }
                }

                else{
                    boolean cond = isValidMarker(-1, isInner, s, i, c);
                    if (cond || !isInner) {
                        /*
                          this is a starting marker
                          the state will add the current flag to its inner flag,
                          e.g. /italic words/ -> we are reaching the first /,
                          in that case, our currentFlag is 0b010000,
                          the state flag could be anything, but because
                          state.hasFlag(currentFlag) is false, it means we need
                          to add a new marker (and to update the state flag to contain
                          the currentFlag)
                         */
                            state.pushWorkStack(new Marker(currentFlag, i));
                    }
                }
            }
        }
        if (state.markers.size() > 1) {
            // we sort the markers by order of appearance
            state.markers.sort((a, b) -> Integer.compare(a.start, b.start));
        }
    }

    public boolean isValidMarker(int a, boolean b, String s, int i, char c){
        return (b && isValidSituation(s.charAt(i + a), s.charAt(i - a), c));
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
            commitFlag(m.type);
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
