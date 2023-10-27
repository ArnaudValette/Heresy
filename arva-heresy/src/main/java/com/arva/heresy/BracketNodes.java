package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

class BracketNode{
    int start;
    int end;
    String type;
    String content;
    public BracketNode(int st){
        start= st;
    }
    public void complete(int e, String t, String s){
        end=e;
        type= t;
        content=s;
    }

    public void describe() {
        System.out.println(content + " " + type);
    }
}

public class BracketNodes{
    final List<BracketNode> brackets = new ArrayList<>();
    BracketNode current;
    List<List<Integer>> toBeFormatted;
    BracketNode prev;

    public void push(BracketNode b) {
        brackets.add(b);
    }

    public void commit(){
        if (prev == null && current.start > 0) {
            pushFormat(0, current.start);
        } else {
            pushFormat(prev.end, current.start);
        }
        brackets.add(current);
        reset();
    }

    public void reset(){
        prev= current;
        current=null; 
    }

    public void start(int start){
        /* When starting a bracketNode
           if the bracketNode is at index > 0
           it means that there's a string toBeFormatted
           between the end of the previous bracketNode and
           the start of that new one;
        
           If there's no previous one AND if start > 0,
           it means the toBeFormatted string is substring(0,start);
         */
        current = new BracketNode(start);
    }
    public void end(int end, String type, String content){
        current.complete(end, type, content);
    }

    public void pushFormat(int formatStart, int formatEnd) {
        List<Integer> l = new ArrayList<>();
        l.add(formatStart);
        l.add(formatEnd);
        toBeFormatted.add(l);
    }

    public void describe() {
        brackets.forEach((b) -> {
            b.describe();
        });
    }
}
