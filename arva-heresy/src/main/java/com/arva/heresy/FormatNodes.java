package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

class FormatNode {
    int start;
    int end;
    int type;
    String content;

    public FormatNode(int s, int e, int t, String c) {
        start = s;
        end = e;
        type = t;
        content = c;
    }

    public void describe() {
        System.out.println();
        System.out.println("-----");
        System.out.println(type);
        System.out.println(content);
        System.out.println("-----");
    }

}


public class FormatNodes {
    final List<FormatNode> formats = new ArrayList<>();

    public void push(int start, int end, int type, String content) {
        formats.add(new FormatNode(start, end, type, content));
    }

    public void push(int start, int end, int type, String content, int loc){
        formats.add(loc, new FormatNode(start, end, type, content));
    }
    public boolean has(int index) {
        return index >= 0 && index < formats.size();
    }
    public FormatNode getLast(){
        return formats.get(formats.size() - 1);
    }

    public FormatNode get(int index) {
        return formats.get(index);
    }

    public void describe() {
        formats.forEach(f -> f.describe());
    }
}
