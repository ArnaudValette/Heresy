package com.arva.heresy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParserConfig implements ParserConfigNode {
    private Node root = new Node();

    public ParserConfig() {
        super();
        String[] cookiePercent = { "digit", "%" };
        String[] cookieRatio = { "digit", "/", "digit" };
        String[] date = { "digit", "-", "digit", "-", "digit", " ", "Capital", "low" , "low"};
        String[] checkBoxEmpty = { " " };
        String[] checkBoxActive = { "X" };
        String[] image = { "[", "any", "]" };
        String[] link = { "[", "any", "]", "[", "ANY", "]" };
        String[] footnote = { "f", "n", ":", "any" };

        subWrapper(cookiePercent, "cookiePercent");
        subWrapper(cookieRatio, "cookieRatio");
        subWrapper(date, "date");
        subWrapper(checkBoxEmpty, "checkBoxEmpty");
        subWrapper(checkBoxActive, "checkBoxActive");
        subWrapper(image, "image");
        subWrapper(link, "link");
        subWrapper(footnote, "footnote");
    }

    public void describe() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(root);
        System.out.println(json);
        //root.describe();
    }


    public void subWrapper(String[] s, String name) {
        subscribe(s, t(name));
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

