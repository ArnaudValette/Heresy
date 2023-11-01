package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

class FormatNode  extends ComparableNode{
    int type;

    public FormatNode(int s, int e, int t, String c) {
        start = s;
        end = e;
        type = t;
        content = c;
    }

    public void describe() {
        System.out.println(content + " " + type + " start: " + start );
    }

}


public class FormatNodes {
    final List<FormatNode> formats = new ArrayList<>();
    int preCount = 0;

    public void pre() {
        preCount += 1;
    }

    public int getPreCount() {
        int value = preCount;
        preCount = 0;
        return value;
    }

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
