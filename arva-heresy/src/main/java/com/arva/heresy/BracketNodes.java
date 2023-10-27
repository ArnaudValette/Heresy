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
    List<List<Integer>> toBeFormatted = new ArrayList<>();
    BracketNode prev;

    public void push(BracketNode b) {
        brackets.add(b);
    }

    public void commit(){
        if (current.start > 0) {
            if (prev == null) {
                pushFormat(0, current.start);
            } else {
                pushFormat(prev.end, current.start);
            }
        }
        brackets.add(current);
        reset();
    }

    public void reset(){
        prev= current;
        current = null;
    }

    public void finalize(int endFormat){
        //  A : dsfsdfsd [dfsdfs] (already handled by commit)
        // B : sdfkjsd sdj sldfsd lkf
        // C : fdlkdsalk fsdkf [fsdf] dfdsfa

        if (prev == null) {
            // B
            pushFormat(0, endFormat);
        }
        else if(prev != null && prev.end < endFormat){
            pushFormat(prev.end, endFormat);
        }
    }

    public void start(int start){
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
        System.out.println(toBeFormatted);
        brackets.forEach((b) -> {
            b.describe();
        });
    }
}
