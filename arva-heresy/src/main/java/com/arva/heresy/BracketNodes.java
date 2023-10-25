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
}

public class BracketNodes{
    final List<BracketNode> brackets = new ArrayList<>();
    BracketNode current;

    public void push(BracketNode b) {
        brackets.add(b);
    }
    public void commit(){
        brackets.add(current);
        reset();
    }
    public void reset(){
        current=null; 
    }

    public void start(int start){
        current = new BracketNode(start);
    }
    public void end(int end, String type, String content){
        current.complete(end, type, content);
    }
}
