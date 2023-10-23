package com.arva.heresy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@FunctionalInterface
interface CommandF {
    boolean apply(char c);
}
public class Parser {
    public Parser() {
    }
}

class Command {
    boolean isRedundant;
    String key;
    CommandF f;

    public Command(CommandF x, boolean r) {
        f = x;
        isRedundant = r;
    }
}

class CommandMap {
    final private Map<String, Command> commands = new HashMap<>();

    public CommandMap() {
    }

    public void subscribeChar(char x){
        put(Character.toString(x), c->x==c, false);
    }

    public void subscribeChar(String chars) {
        for (int i = 0, j = chars.length(); i < j; i = i + 1) {
            subscribeChar(chars.charAt(i));
        }
    }
    public void put(String key, CommandF f, boolean r){
        commands.put(key,new Command(f,r));
    }

    public void testAll(char c) {
        commands.forEach((key,value)->{
            if (value.f.apply(c)) {
                System.out.println();
                System.out.println(c);
                System.out.println(key);
                System.out.println(value.f.apply(c));
                System.out.println();
            }
                    });
    }
}

class BracketParser extends Parser {
    ParserConfig config;
    CommandMap commands;
    ParserConfig curr;  
    public BracketParser(ParserConfig conf) {
        config = conf;
        commands = fromConfigToCommands();
    }

    public CommandMap fromConfigToCommands() {
        CommandMap map = new CommandMap();
        map.put("digit", c -> Character.isDigit(c), true);
        map.put("Capital", c -> Character.isUpperCase(c) , false);
        map.put("low", c -> Character.isLowerCase(c) , false);
        map.put("any", c -> c != ' ' && c != '[' && c != ']', true);
        map.put("ANY", c -> c != '[' && c != ']', true);
        map.subscribeChar("%/- X[]fn:");
        return map;
    }
    public void parse(String s) {
//* A title with a [[~/.emacs.d/init.el][file]] and an Image : [[~/desk]]
        curr = config;
        for (int i = 0, j = s.length(); i < j; i = i + 1) {
            //commands.testAll(s.charAt(i));
        }
        System.out.println(s);
    }
}
