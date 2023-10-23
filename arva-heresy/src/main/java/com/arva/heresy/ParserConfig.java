package com.arva.heresy;


public class ParserConfig implements ParserConfigNode {
    private Node root = new Node();

    public ParserConfig() {
        String[][] config = {
            {"digit,%", "cookiePercent"},
            {"digit,/,digit","cookieRatio"},
            {"digit,-,digit,-,digit, ,Capital,low,low","date"},
            {" ","checkBoxEmpty"},
            {"X","checkBoxActive"},
            {"[,any,]","image"},
            {"[,any,],[,ANY,]","link"},
            {"f,n,:,any","footnote"},
        };
        for(String[] elemt : config){
            subWrapper(elemt);
        }
    }

    public void describe() {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //String json = gson.toJson(root);
        //System.out.println(json);
        root.describe();
    }

    public ParserConfigNode get(String key){
        return (ParserConfigNode) root.get(key);
    }

    public boolean has(String key){
        return root.has(key);
    }


    public void subWrapper(String[] s) {
        subscribe(arr(s[0]), t(s[1]));
    }

    public String[] arr(String s) {
        return s.split(",");
    }

    public void subscribe(String[] s, Tail t) {
        Node curr = root;
        for (int i = 0, j = s.length; i < j; i = i + 1) {
            String c = s[i];
            if (!curr.has(c)) {
                curr.put(c, new Node());
            }

            curr = (Node) curr.get(c);
        }
        curr.put("]", t);
    }
    public Tail t(String name) {
        return new Tail(name);
    }


}

