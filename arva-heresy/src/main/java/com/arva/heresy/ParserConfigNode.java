package com.arva.heresy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

interface ParserConfigNode {
    void describe();
    ParserConfigNode get(String key);
    boolean has(String key);
    void put(String key, ParserConfigNode n);
    Set<Entry<String, ParserConfigNode>> entrySet();
    Set<String> keySet();
}

/*
  NB this class should not really be a ParserConfigNode, but it it necessary for me
  to do so in order for ParserConfig to be able to deal with both Tail and Node;
*/
class Tail implements ParserConfigNode {
    boolean done;
    String type;

    public Tail(String t) {
        type = t;
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public String getType() {
        return type;
    }

    public void describe() {
        System.out.println(getType());
    }

    public ParserConfigNode get(String key) {
        return this;
    }

    public boolean has(String key) {
        return false;
    }

    public void put(String key, ParserConfigNode n) {
    }

    public Set<Entry<String, ParserConfigNode>> entrySet() {
        Set<Entry<String, ParserConfigNode>> res = new HashSet<>();
        res.add(Map.entry("done", this));
        return res;
    }

    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        res.add(type);
        return res;
    }

}


class Node implements ParserConfigNode {
    // At one point Node is transitionning from Map<String, Node> to Map<String, Tail>
    // I don't know yet how to do better (typescript has this ability to Map<String , Node | Tail> 
    // which prevents us from creating this kind of artificial "implementation" to regroup things under a common type
    final Map<String, ParserConfigNode> children = new HashMap<>();

    public Set<String> keySet(){
        return children.keySet();
    }
    public Set<Entry<String,ParserConfigNode>> entrySet(){
        return children.entrySet();
    }

    public void put(String key, ParserConfigNode child) {
        children.put(key, child);
    }

    public ParserConfigNode get(String key) {
        return children.get(key);
    }

    public boolean has(String key) {
        return children.containsKey(key);
    }

    public void describe() {
        Set<Entry<String,ParserConfigNode>> entries = entrySet();
        for(Entry<String, ParserConfigNode> entry:entries){
            System.out.println(entry.getKey());
            ParserConfigNode val =  entry.getValue();
            val.describe();
        }
    }
}

