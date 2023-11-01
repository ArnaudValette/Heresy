package com.arva.heresy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class pNodes<T extends pNode> {
    private Class<T> type;
    final List<T> nodes = new ArrayList<>();
    T current;
    T prev;
    List<List<Integer>> toBeFormatted = new ArrayList<>();

    public pNodes(Class<T> type) {
        this.type = type;
    }

    public int size() {
        return nodes.size();
    }

    public T get(int index) {
        return nodes.get(index);
    }

    public boolean has(int index) {
        return index >= 0 && index < nodes.size();
    }

    public void push(T b) {
        nodes.add(b);
    }

    public void paragraphNode(int len) {
        pushFormat(0, len);
        nodes.add(current);
        reset();
    }

    public void commit() {
        if (current.start > 0) {
            if (prev == null) {
                pushFormat(0, current.start);
            } else {
                pushFormat(prev.end + 1, current.start);
            }
        }
        nodes.add(current);
        reset();
    }

    public void reset() {
        prev = current;
        current = null;
    }

    public void finalize(int endFormat) {
        if (prev == null) {
            pushFormat(0, endFormat);
        } else if (prev != null && prev.end < endFormat) {
            pushFormat(prev.end + 1, endFormat);
        }
        // You only have the indexes of the substrings to be formatted,
        // this is not here that you should use a callback to parse Format nodes
    }

    public T createT(int start) {
        try {
            return (T) type.getDeclaredConstructor(int.class).newInstance(start);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create instance of " + type.getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance of " + type.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create instance of " + type.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot create instance of " + type.getName(), e);
        }
    }

    public void start(int start) {
        current = createT(start);
    }

    public void end(int end, String type, String content) {
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
        nodes.forEach((b) -> {
            b.describe();
        });
    }

}

class ComparableNode {
    int start;
    int end;
    String content;

    public void describe() {
    }
}

class pNode extends ComparableNode{
    String type;

    public pNode(int st){
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
