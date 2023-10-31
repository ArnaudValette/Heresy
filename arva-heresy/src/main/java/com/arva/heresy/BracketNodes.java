package com.arva.heresy;

class BracketNode extends pNode{
    public BracketNode(int st){
        super(st);
    }
}

public class BracketNodes extends pNodes<BracketNode>{
    public BracketNodes(){
        super(BracketNode.class);
    }

    // public void start(int start){
    //     current = new BracketNode(start);
    // }
}
