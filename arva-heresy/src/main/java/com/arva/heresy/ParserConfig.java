package com.arva.heresy;

import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Set;

public class ParserConfig implements ParserConfigNode {
    final ParserConfigNode children = new Node();

    public ParserConfig(String[][] config) {
        for(String[] elemt : config){
            subWrapper(elemt);
        }
    }

    public void describe() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(children);
        System.out.println(json);
        children.describe();
    }

    public void subWrapper(String[] s) {
        subscribe(arr(s[0]), t(s[1]));
    }

    public String[] arr(String s) {
        return s.split(",");
    }

    public void subscribe(String[] s, Tail t) {
        ParserConfigNode curr = children;
        for (int i = 0, j = s.length - 1; i < j; i = i + 1) {
            String c = s[i];
            if (!curr.has(c)) {
                curr.put(c, new Node());
            }

            curr = curr.get(c);
        }
        curr.put(s[s.length-1], t);
    }

    public Tail t(String name) {
        return new Tail(name);
    }

    public Node toNode(){
        return (Node) this.children;
    }

    @Override
    public ParserConfigNode get(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public boolean has(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'has'");
    }

    @Override
    public void put(String key, ParserConfigNode n) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public Set<Entry<String, ParserConfigNode>> entrySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entrySet'");
    }

    @Override
    public List<String> keySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keySet'");
    }


}
