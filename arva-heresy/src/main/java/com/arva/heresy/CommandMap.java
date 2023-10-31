package com.arva.heresy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@FunctionalInterface
interface CommandF {
    boolean apply(char c);
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

public class CommandMap {
    final private Map<String, Command> commands = new HashMap<>();

    public CommandMap() {
    }

    public void subscribeChar(char x){
        put(Character.toString(x), c->x==c, false);
    }

    public Set<Entry<String, Command>> entrySet(){
        return commands.entrySet();
    }

    public void subscribeChars(String chars) {
        for (int i = 0, j = chars.length(); i < j; i = i + 1) {
            subscribeChar(chars.charAt(i));
        }
    }
    public void put(String key, CommandF f, boolean r){
        commands.put(key,new Command(f,r));
    }

    public Command get(String key){
        return commands.get(key);
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
