package com.arva.heresy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

interface ParserConfigNode {
    void describe();
}
class Tail implements ParserConfigNode {
    boolean done;
    String type;

    public Tail(String t) {
        type = t;
        done = true;
    }

    public boolean isDone(){
        return done;
    }

    public String getType() {
        return type;
    }

    public void describe(){
        System.out.println(getType());
    }
}

class Node implements ParserConfigNode {
    final private Map<String, ParserConfigNode> children= new HashMap<>();

    public Set<Entry<String,ParserConfigNode>> entrySet(){
        return children.entrySet();
    }

    public void put(String key, ParserConfigNode child) {
        children.put(key, child);
    }

    public Object get(String key) {
        return children.get(key);
    }

    public boolean has(String key) {
        return children.containsKey(key);
    }

    public void describe() {
        Set<Entry<String,ParserConfigNode>> entries = entrySet();
        System.out.print(entries);
        for(Entry<String, ParserConfigNode> entry:entries){
            System.out.println(entry.getKey());
            ParserConfigNode val =  entry.getValue();
            val.describe();
        }
    }
}

