package com.arva.heresy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

    public Set<Entry<String, Command>> entrySet(){
        return commands.entrySet();
    }

    public void subscribeChar(String chars) {
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

class BracketParser extends Parser {
    ParserConfig config;
    CommandMap commands;
    String prevKey;
    Node prev;
    Node curr;  
    String memory;
    BracketNodes brackets;
    boolean isActive;
    boolean hasMatched;
    boolean isRedundant;
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
    public void memoryInit(){
        memory = "";
    }
    public void memoryAppend(char c){
        memory = memory + Character.toString(c);
    }
    public void parse(String s) {
//* A title with a [[~/.emacs.d/init.el][file]] and an Image : [[~/desk]]
        brackets = new BracketNodes();
        noMatch();
        for (int i = 0, j = s.length(); i < j; i = i + 1) {
            char c = s.charAt(i);
            Set<String> keys= curr.keySet();
            Iterator<String> iterator = keys.iterator();

            while(iterator.hasNext()){
                String key = iterator.next();
                handleMatch(key, c);
                if(hasMatched){
                    if(!isActive){
                        brackets.prepareBracket(i);
                    }
                    if(curr.get(key) instanceof Node){
                        handleNode(curr, key, c);
                        break;
                    }
                    if(curr.get(key) instanceof Tail){
                        handleTail((Tail) curr.get(key), c, i);
                        break;
                    }
                }
            }
            //after while
            if(!hasMatched){
                if(isRedundant){
                    handleMatch(prevKey, c);
                    if(hasMatched){
                        handleNode(prev, prevKey, c);
                    }
                    else{
                        noMatch();
                    }
                }
                else{
                    noMatch();
                }
            }
        }
    }

    public void handleNode(Node newPrev, String key, char c){
        prev = isRedundant ? newPrev : null;
        curr= (Node) curr.get(key);
        prevKey = isRedundant ? key : null;
        memoryAppend(c);
        isActive = true;
        hasMatched = true;
    }
    public void handleMatch(String key, char c){
        Command comm = commands.get(key);
        hasMatched = comm.f.apply(c);
        isRedundant = comm.isRedundant;
    }

    public void handlePrevMatch(String key, char c){
        Command comm = commands.get(key);
        hasMatched= comm.f.apply(c);
        isRedundant= comm.isRedundant;
        prev=isRedundant ? prev : null; 
        prevKey = isRedundant ? key : null;
    }
    public void handleTail(Tail t, char c, int i){
        memoryAppend(c);
        brackets.completeBracket(i, t.type, memory);
        brackets.publish();
        noMatch();
    }
    public void noMatch(){
        curr=config;
        memoryInit();
        isActive=false;
        hasMatched=false;
        isRedundant= false;
        prev= null;
    }
}

class BracketNode{
    int start;
    int end;
    String type;
    String content;
    public BracketNode(int st){
        start= st;
    }
    public void complete(int e, String t, String s){
        end=e;
        type= t;
        content=s;
    }
}

class BracketNodes{
    final List<BracketNode> brackets = new ArrayList<>();
    BracketNode current;

    public void push(BracketNode b) {
        brackets.add(b);
    }
    public void publish(){
        brackets.add(current);
        destroyCurrent();
    }
    public void destroyCurrent(){
        current=null; 
    }

    public void prepareBracket(int start){
        current = new BracketNode(start);
    }
    public void completeBracket(int end, String type, String content){
        current.complete(end, type, content);
    }
}
