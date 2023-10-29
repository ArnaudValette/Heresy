package com.arva.heresy;
import java.util.List;

class HornNode extends pNode{
    int lineNumber;
    int level;
    int id;
    List<HornNode> children;
    String textContent;
    String type;

    public HornNode(int ln, String line) {
        super(ln);
        lineNumber = ln;

    }
}

public class HornNodes extends pNodes<HornNode> {
    public HornNodes() {
        super(HornNode.class);
    }

    public void finalize() {
    }

}

