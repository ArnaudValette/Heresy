package com.arva.heresy;
import java.util.List;

public class HornNode {
    int lineNumber;
    int level;
    int id;
    List<HornNode> children;
    String textContent;
    String type;

    public HornNode(int ln, String line) {
        lineNumber = ln;

    }
}
