package com.arva.heresy;
import java.util.List;

class HornNode extends pNode{
    List<HornNode> children;
    List<? extends ComparableNode> lesserElements;

    public HornNode(int ln) {
        super(ln);
    }
}

public class HornNodes extends pNodes<HornNode> {
    public HornNodes() {
        super(HornNode.class);
    }
}

